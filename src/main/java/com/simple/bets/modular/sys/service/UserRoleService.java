package com.simple.bets.modular.sys.service;

import com.simple.bets.core.common.util.Page;
import com.simple.bets.modular.sys.model.Role;
import com.simple.bets.modular.sys.model.User;
import com.simple.bets.modular.sys.model.UserRole;
import com.simple.bets.core.base.service.IService;

public interface UserRoleService extends IService<UserRole> {

	/**
	 * 分页 查询角色对应的用户
	 * @param page
	 * @param user
	 * @param exist true 存在 false 不存在
	 * @return
	 */
	Page<User> queryPage(Page<User> page, User user,Boolean exist);

	/**
	 * 授权用户 角色
	 * @param role
	 */
	void assignRoleUser(Role role);

	/**
	 * 删除该角色对应的所有用户
	 * @param roleIds
	 */

	void deleteAllUserRolesByRoleId(String roleIds);

	/**
	 * 通过角色删除用户授权
	 * @param role
	 */
	void deleteUserRolesByRoleId(Role role);

	void deleteUserRolesByUserId(String userIds);
}
