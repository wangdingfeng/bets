package com.simple.bets.modular.sys.service.impl;

import com.simple.bets.core.common.util.TreeUtils;
import com.simple.bets.core.model.Tree;
import com.simple.bets.modular.sys.dao.MenuMapper;
import com.simple.bets.modular.sys.model.Menu;
import com.simple.bets.core.service.impl.ServiceImpl;
import com.simple.bets.modular.sys.service.MenuService;
import com.simple.bets.modular.sys.service.RoleMenuService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.*;

@Service("menuService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl extends ServiceImpl<Menu> implements MenuService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private WebApplicationContext applicationContext;

    @Override
    public List<Menu> findUserPermissions(String userName) {
        return this.menuMapper.findUserPermissions(userName);
    }

    @Override
    public List<Menu> findUserMenus(String userName) {
        return this.menuMapper.findUserMenus(userName);
    }

    @Override
    public List<Menu> findAllMenus(Menu menu) {
        try {
            Example example = new Example(Menu.class);
            Criteria criteria = example.createCriteria();
            if (StringUtils.isNotBlank(menu.getMenuName())) {
                criteria.andCondition("menu_name=", menu.getMenuName());
            }
            if (StringUtils.isNotBlank(menu.getType())) {
                criteria.andCondition("type=", Long.valueOf(menu.getType()));
            }
            criteria.andCondition("parent_id=", null != menu.getParentId() ? menu.getParentId() : 0);
            example.setOrderByClause("id");
            return this.selectByExample(example);
        } catch (NumberFormatException e) {
            log.error("error", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Tree<Menu> getMenuTree() {
        List<Tree<Menu>> trees = new ArrayList<>();
        Example example = new Example(Menu.class);
        example.createCriteria().andCondition("type =", 0);
        example.setOrderByClause("create_time");
        List<Menu> menus = this.selectByExample(example);
        buildTrees(trees, menus);
        return TreeUtils.build(trees);
    }

    private void buildTrees(List<Tree<Menu>> trees, List<Menu> menus) {
        menus.forEach(menu -> {
            Tree<Menu> tree = new Tree<>();
            tree.setId(menu.getId().toString());
            tree.setParentId(menu.getParentId().toString());
            tree.setText(menu.getMenuName());
            trees.add(tree);
        });
    }

    @Override
    public List<Tree<Menu>> getUserMenu(String userName) {
        List<Tree<Menu>> trees = new ArrayList<>();
        List<Menu> menus = this.findUserMenus(userName);
        menus.forEach(menu -> {
            Tree<Menu> tree = new Tree<>();
            tree.setId(menu.getId().toString());
            tree.setParentId(menu.getParentId().toString());
            tree.setText(menu.getMenuName());
            tree.setIcon(menu.getIcon());
            tree.setUrl(menu.getUrl());
            tree.setTargetType(menu.getTargetType());
            trees.add(tree);
        });
        return TreeUtils.buildList(trees);
    }

    @Override
    public Menu findByNameAndType(String menuName, String type) {
        Example example = new Example(Menu.class);
        example.createCriteria().andCondition("lower(menu_name)=", menuName.toLowerCase())
                .andEqualTo("type", Long.valueOf(type));
        List<Menu> list = this.selectByExample(example);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    @Transactional
    public void addMenu(Menu menu) {
        menu.setCreateTime(new Date());
        if (menu.getParentId() == null)
            menu.setParentId(0L);
        if (Menu.TYPE_BUTTON.equals(menu.getType())) {
            menu.setUrl(null);
            menu.setIcon(null);
        }
        this.save(menu);
    }

    @Override
    @Transactional
    public void deleteMeuns(String menuIds) {
        List<String> list = Arrays.asList(menuIds.split(","));
        this.batchDelete(list, "menuId", Menu.class);
        this.roleMenuService.deleteRoleMenusByMenuId(menuIds);
        this.menuMapper.changeToTop(list);
    }

    @Override
    public List<Map<String, String>> getAllUrl(String p1) {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取 url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        List<Map<String, String>> urlList = new ArrayList<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            HandlerMethod handlerMethod = map.get(info);
            RequiresPermissions permissions = handlerMethod.getMethodAnnotation(RequiresPermissions.class);
            String perms = "";
            if (null != permissions) {
                perms = StringUtils.join(permissions.value(), ",");
            }
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            for (String url : patterns) {
                Map<String, String> urlMap = new HashMap<>();
                urlMap.put("url", url.replaceFirst("\\/", ""));
                urlMap.put("perms", perms);
                urlList.add(urlMap);
            }
        }
        return urlList;

    }

    @Override
    public void saveOrUpdate(Menu menu) {

        // 获取父节点实体
        Menu parent = super.findById(menu.getParentId());
        Long oldParentId = null;
        String oldParentIds = "";
        int oldParentTLevel = 0;

        if(null == menu.getId()){
            //新增根节点
            if(null == parent){
                menu.setTreeLeaf("1");
                menu.setTreeLevel(0);
            }else{
                menu.setTreeLeaf("1");
                menu.setTreeLevel(parent.getTreeLevel() + 1);
                if (parent.getIsTreeLeaf()) {
                    parent.setTreeLeaf("0");
                    super.updateNotNull(parent);
                }
            }

            // 设置新的父节点串
            menu.setParentIds(parent == null ? "0," : (parent.getParentIds() + menu.getParentId() + ","));
            menu.setBaseData(true);
            super.save(menu);

        }else{
            // 数据库中当前的menu的ParentId，还未更新
            oldParentId = findById(menu.getId()).getParentId();
            // 获取修改前的parentIds，用于更新子节点的parentIds
            oldParentIds = menu.getParentIds();

            Menu oldParent = findById(oldParentId);
            oldParentTLevel = oldParent == null ? -1 : oldParent.getTreeLevel();

            // 设置新的父节点串
            menu.setParentIds(parent == null ? "0," : (parent.getParentIds() + menu.getParentId() + ","));
            menu.setBaseData(false);
            super.updateNotNull(menu);

            // 判断menu父节点是否发生了改变
            // 1.menu修改父节点的情况： 如果原来的父节点只有一个子节点，menu修改了父节点之后，
            //   原来的父节点没有子节点了，对应的需要修改原来的父节点的treeLeaf属性为1
            // 2.如果menu父节点发生了改变，menu的所有子节点的parentIds、treeLevel都需要更新
            if (!oldParentId.equals(menu.getParentId())) {
                // 第一步：判断原来的父节点下还有没有子菜单
                if (oldParent != null) {
                    List<Menu> list1 = this.menuMapper.findSubMenuListByPid(oldParent.getParentId());
                    // 原来的父节点下没有子节点了，并且节点treeleaf属性不等于1
                    if (list1.size() <= 0 && !oldParent.getIsTreeLeaf()) {
                        oldParent.setTreeLeaf("1");
                        super.updateNotNull(oldParent);
                    }
                }

                // 第二步：1.更新子节点 parentIds
                // 2.menu修改了父节点，则需要根据修改后的父节点treeLevel，
                //   更新menu及menu子节点的treelevel值，这样才能保证树结构的层级
                List<Menu> list2 = findSubMenuListByPid(menu.getId());

                Menu newParent = findById(menu.getParentId());
                int diffValue = newParent.getTreeLevel() - oldParentTLevel;

                // 更新menu的treelevel值
                menu.setTreeLevel(menu.getTreeLevel() + diffValue);
                this.updateAll(menu);

                for (Menu e : list2) {
                    // 更新子节点 parentIds
                    e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
                    // 更新menu子节点的treelevel值
                    e.setTreeLevel(e.getTreeLevel() + diffValue);
                    this.updateNotNull(e);
                }

                // 第三步：新父节点如果treeLeaf==1，则需要更新treeLeaf==0
                if (newParent.getIsTreeLeaf()) {
                    newParent.setTreeLeaf("0");
                    this.updateNotNull(newParent);
                }
            }


        }




    }

    @Override
    public Menu findById(Long menuId) {
        return this.selectByKey(menuId);
    }

    @Override
    @Transactional
    public void updateMenu(Menu menu) {
        menu.setModifyTime(new Date());
        if (menu.getParentId() == null)
            menu.setParentId(0L);
        if (Menu.TYPE_BUTTON.equals(menu.getType())) {
            menu.setUrl(null);
            menu.setIcon(null);
        }
        this.updateNotNull(menu);
    }

    private List<Menu> findSubMenuListByPid(Long menuId){
        Example example = new Example(Menu.class);
        example.createCriteria().andLike("parent_ids",","+menuId+",");
        List<Menu> list = this.selectByExample(example);
        return list;
    }

}
