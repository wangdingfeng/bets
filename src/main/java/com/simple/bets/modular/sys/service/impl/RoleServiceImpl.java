package com.simple.bets.modular.sys.service.impl;

import com.simple.bets.core.shiro.ShiroRealm;
import com.simple.bets.modular.sys.dao.RoleMapper;
import com.simple.bets.modular.sys.dao.RoleMenuMapper;
import com.simple.bets.modular.sys.model.RoleModel;
import com.simple.bets.modular.sys.model.RoleMenuModel;
import com.simple.bets.core.base.service.impl.ServiceImpl;
import com.simple.bets.modular.sys.service.RoleMenuService;
import com.simple.bets.modular.sys.service.RoleService;
import com.simple.bets.modular.sys.service.UserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author wangdingfeng
 * @Description 角色服务
 * @Date 18:57 2019/1/23
 **/

@Service("roleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleModel> implements RoleService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private ShiroRealm shiroRealm;

    @Override
    public List<RoleModel> findUserRole(String userName) {
        return this.roleMapper.findUserRole(userName);
    }

    @Override
    public RoleModel saveOrUpdate(RoleModel role) {
        if(null != role.getRoleId() ){
            role.setBaseData(false);
            super.updateNotNull(role);
            //删除已有菜单
            Example example = new Example(RoleMenuModel.class);
            example.createCriteria().andCondition("role_id=", role.getRoleId());
            this.roleMenuMapper.deleteByExample(example);
            setRoleMenus(role);
        }else{
            role.setBaseData(true);
            super.save(role);
            setRoleMenus(role);
        }
        shiroRealm.clearCache();
        return role;
    }
    @Override
    public List<RoleModel> findAllRole(RoleModel role) {
        try {
            Example example = new Example(RoleModel.class);
            if (StringUtils.isNotBlank(role.getRoleName())) {
                example.createCriteria().andCondition("role_name=", role.getRoleName());
            }
            example.setOrderByClause("create_time");
            return this.selectByExample(example);
        } catch (Exception e) {
            log.error("获取角色信息失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public RoleModel findByName(String roleName) {
        Example example = new Example(RoleModel.class);
        example.createCriteria().andCondition("lower(role_name)=", roleName.toLowerCase());
        List<RoleModel> list = this.selectByExample(example);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 设置角色菜单
     * @param role
     */
    private void setRoleMenus(RoleModel role) {
        if(StringUtils.isEmpty(role.getMenuIds())){
            return;
        }
        String[] menuIdJson = role.getMenuIds().split(",");
        Arrays.stream(menuIdJson).forEach(menuId -> {
            RoleMenuModel rm = new RoleMenuModel();
            rm.setMenuId(Long.parseLong(menuId));
            rm.setRoleId(role.getRoleId());
            this.roleMenuMapper.insert(rm);
        });
    }

    @Override
    @Transactional
    public void deleteRoles(String roleIds) {
        List<String> list = Arrays.asList(roleIds.split(","));
        this.batchDelete(list, "roleId", RoleModel.class);
        this.roleMenuService.deleteRoleMenusByRoleId(roleIds);
        this.userRoleService.deleteAllUserRolesByRoleId(roleIds);

    }

}
