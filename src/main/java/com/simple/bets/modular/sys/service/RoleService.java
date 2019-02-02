package com.simple.bets.modular.sys.service;


import com.simple.bets.modular.sys.model.Role;
import com.simple.bets.modular.sys.model.RoleWithMenu;
import com.simple.bets.core.service.IService;

import java.util.List;
/**
 * @Author wangdingfeng
 * @Description 角色管理
 * @Date 18:55 2019/1/23
 **/

public interface RoleService extends IService<Role> {

	/**
	 * 查询用户角色信息
	 * @param userName
	 * @return
	 */
	List<Role> findUserRole(String userName);

	/**
	 * 保存|修改
	 * @param role
	 * @return
	 */
	Role saveOrUpdate(Role role);

	List<Role> findAllRole(Role role);
	
	RoleWithMenu findRoleWithMenus(Long roleId);

	Role findByName(String roleName);

	void addRole(Role role, Long[] menuIds);
	
	void updateRole(Role role, Long[] menuIds);
	/**
	 * 批量删除角色
	 * @param roleIds
	 */
	void deleteRoles(String roleIds);
}
