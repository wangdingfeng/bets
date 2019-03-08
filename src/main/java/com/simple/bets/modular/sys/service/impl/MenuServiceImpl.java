package com.simple.bets.modular.sys.service.impl;

import com.simple.bets.core.common.util.TreeUtils;
import com.simple.bets.core.base.model.Tree;
import com.simple.bets.modular.sys.dao.MenuMapper;
import com.simple.bets.modular.sys.model.MenuModel;
import com.simple.bets.core.base.service.impl.ServiceImpl;
import com.simple.bets.modular.sys.service.MenuService;
import org.apache.commons.lang3.StringUtils;
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
public class MenuServiceImpl extends ServiceImpl<MenuModel> implements MenuService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private WebApplicationContext applicationContext;

    @Override
    public List<MenuModel> findUserPermissions(String userName) {
        return this.menuMapper.findUserPermissions(userName);
    }

    @Override
    public List<MenuModel> findUserMenus(String userName) {
        return this.menuMapper.findUserMenus(userName);
    }

    @Override
    public List<MenuModel> findAllMenus(MenuModel menu) {
        try {
            Example example = new Example(MenuModel.class);
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
    public Tree<MenuModel> getMenuTree(boolean isAll) {
        List<Tree<MenuModel>> trees = new ArrayList<>();
        Example example = new Example(MenuModel.class);
        if(!isAll){
            example.createCriteria().andCondition("type =", 0);
        }
        example.setOrderByClause("create_time");
        List<MenuModel> menus = this.selectByExample(example);
        buildTrees(trees, menus);
        return TreeUtils.build(trees);
    }

    private void buildTrees(List<Tree<MenuModel>> trees, List<MenuModel> menus) {
        menus.forEach(menu -> {
            Tree<MenuModel> tree = new Tree<>();
            tree.setId(menu.getId().toString());
            tree.setParentId(menu.getParentId().toString());
            tree.setText(menu.getMenuName());
            trees.add(tree);
        });
    }

    @Override
    public List<Tree<MenuModel>> getUserMenu(String userName) {
        List<Tree<MenuModel>> trees = new ArrayList<>();
        List<MenuModel> menus = this.findUserMenus(userName);
        menus.forEach(menu -> {
            Tree<MenuModel> tree = new Tree<>();
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
    public MenuModel findByNameAndType(String menuName, String type) {
        Example example = new Example(MenuModel.class);
        example.createCriteria().andCondition("lower(menu_name)=", menuName.toLowerCase())
                .andEqualTo("type", Long.valueOf(type));
        List<MenuModel> list = this.selectByExample(example);
        return list.isEmpty() ? null : list.get(0);
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
    @Transactional(readOnly = false)
    public void saveOrUpdate(MenuModel menu) {

        // 获取父节点实体
        MenuModel newParent = super.findById(menu.getParentId());
        Long oldParentId = null;
        String oldParentIds = "";
        int oldParentTLevel = 0;

        if(null == menu.getId()){
            //新增根节点
            if(null == newParent){
                menu.setTreeLeaf(MenuModel.TREE_LEAF_NO);
                menu.setTreeLevel(0);
            }else{
                menu.setTreeLeaf(MenuModel.TREE_LEAF_NO);
                menu.setTreeLevel(newParent.getTreeLevel() + 1);
                if (newParent.getIsTreeLeaf()) {
                    newParent.setTreeLeaf(MenuModel.TREE_LEAF_YES);
                    super.updateNotNull(newParent);
                }
            }

            // 设置新的父节点串
            menu.setParentIds(newParent == null ? "0" : (newParent.getParentIds()+","+ menu.getParentId()));
            menu.setBaseData(true);
            super.save(menu);

        }else{
            // 数据库中当前的menu的ParentId，还未更新
            MenuModel copyMenu =  findById(menu.getId());
            oldParentId = copyMenu.getParentId();
            // 获取修改前的parentIds，用于更新子节点的parentIds
            oldParentIds = copyMenu.getParentIds();


            MenuModel oldParent = findById(oldParentId);
            oldParentTLevel = oldParent == null ? -1 : oldParent.getTreeLevel();

            // 设置新的父节点串
            if(!oldParentId.equals(menu.getParentId())){
                menu.setParentIds(newParent == null ? "0" : (newParent.getParentIds() +","+ menu.getParentId() ));
                //获取新的父类层级
                menu.setTreeLevel(newParent.getTreeLevel()+1);
            }
            menu.setBaseData(false);
            super.updateNotNull(menu);

            // 判断menu父节点是否发生了改变
            if (!oldParentId.equals(menu.getParentId())) {
                // 第一步：判断原来的父节点下还有没有子菜单
                if (oldParent != null) {
                    List<MenuModel> list1 = this.menuMapper.findSubMenuListByPid(oldParentId);
                    // 原来的父节点下没有子节点了，并且节点treeleaf属性不等于1
                    if (list1.size() <= 0 && !oldParent.getIsTreeLeaf()) {
                        oldParent.setTreeLeaf(MenuModel.TREE_LEAF_NO);
                        super.updateNotNull(oldParent);
                    }
                }

                // 第二步：1.更新子节点 parentIds
                List<MenuModel> list2 = findSubMenuListByPid(menu.getId());

                int diffValue = newParent.getTreeLevel() - oldParentTLevel;

                for (MenuModel e : list2) {
                    // 更新子节点 parentIds
                    e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
                    // 更新menu子节点的treelevel值
                    e.setTreeLevel(e.getTreeLevel() + diffValue);
                    this.updateNotNull(e);
                }

                // 第三步：新父节点如果treeLeaf==1，则需要更新treeLeaf==0
                if (newParent.getIsTreeLeaf()) {
                    newParent.setTreeLeaf(MenuModel.TREE_LEAF_YES);
                    this.updateNotNull(newParent);
                }
            }

        }

    }

    @Override
    public MenuModel findById(Long menuId) {
        return this.selectByKey(menuId);
    }


    /**
     * 更新排序
     * @param menu
     */
    @Transactional(readOnly = false)
    public void updateMenuSort(MenuModel menu) {
        menuMapper.updateSort(menu);
    }

    @Override
    public void deleteMenu(Long id) {
        //查询父类是无此节点
        MenuModel menu = findById(id);
        //查询父类
        MenuModel menuParent = findById(menu.getParentId());
        if(null != menuParent){
            //查询所有的子类
            MenuModel menuChild = new MenuModel();
            menuChild.setParentId(menu.getParentId());
            List<MenuModel> list = super.queryObjectForList(menuChild);
            if(list.size() == 1){
                menuParent.setTreeLeaf(MenuModel.TREE_LEAF_NO);
                this.updateNotNull(menuParent);
            }
        }
        this.delete(id);
    }

    private List<MenuModel> findSubMenuListByPid(Long menuId){
        Example example = new Example(MenuModel.class);
        example.createCriteria().andLike("parentIds",","+menuId+",");
        List<MenuModel> list = this.selectByExample(example);
        return list;
    }

}
