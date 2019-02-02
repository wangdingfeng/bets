package com.simple.bets.modular.sys.service.impl;

import com.simple.bets.modular.sys.dao.RoleMapper;
import com.simple.bets.modular.sys.dao.RoleMenuMapper;
import com.simple.bets.modular.sys.model.Role;
import com.simple.bets.modular.sys.model.RoleMenu;
import com.simple.bets.modular.sys.model.RoleWithMenu;
import com.simple.bets.core.service.impl.ServiceImpl;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author wangdingfeng
 * @Description 角色服务
 * @Date 18:57 2019/1/23
 **/

@Service("roleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<Role> implements RoleService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<Role> findUserRole(String userName) {
        return this.roleMapper.findUserRole(userName);
    }

    @Override
    public Role saveOrUpdate(Role role) {
        if(null != role.getRoleId() ){
            role.setModifyTime(new Date());
            super.updateNotNull(role);
        }else{
            role.setCreateTime(new Date());
            role.setModifyTime(new Date());
            super.save(role);
        }
        return role;
    }
    @Override
    public List<Role> findAllRole(Role role) {
        try {
            Example example = new Example(Role.class);
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
    public Role findByName(String roleName) {
        Example example = new Example(Role.class);
        example.createCriteria().andCondition("lower(role_name)=", roleName.toLowerCase());
        List<Role> list = this.selectByExample(example);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    @Transactional
    public void addRole(Role role, Long[] menuIds) {
        role.setCreateTime(new Date());
        this.save(role);
        setRoleMenus(role, menuIds);
    }

    private void setRoleMenus(Role role, Long[] menuIds) {
        Arrays.stream(menuIds).forEach(menuId -> {
            RoleMenu rm = new RoleMenu();
            rm.setMenuId(menuId);
            rm.setRoleId(role.getRoleId());
            this.roleMenuMapper.insert(rm);
        });
    }

    @Override
    @Transactional
    public void deleteRoles(String roleIds) {
        List<String> list = Arrays.asList(roleIds.split(","));
        this.batchDelete(list, "roleId", Role.class);
        this.roleMenuService.deleteRoleMenusByRoleId(roleIds);
        this.userRoleService.deleteUserRolesByRoleId(roleIds);

    }

    @Override
    public RoleWithMenu findRoleWithMenus(Long roleId) {
        List<RoleWithMenu> list = this.roleMapper.findById(roleId);
        List<Long> menuList = list.stream().map(RoleWithMenu::getMenuId).collect(Collectors.toList());
        if (list.isEmpty())
            return null;
        RoleWithMenu roleWithMenu = list.get(0);
        roleWithMenu.setMenuIds(menuList);
        return roleWithMenu;
    }

    @Override
    @Transactional
    public void updateRole(Role role, Long[] menuIds) {
        role.setModifyTime(new Date());
        this.updateNotNull(role);
        Example example = new Example(RoleMenu.class);
        example.createCriteria().andCondition("role_id=", role.getRoleId());
        this.roleMenuMapper.deleteByExample(example);
        setRoleMenus(role, menuIds);
    }

}
