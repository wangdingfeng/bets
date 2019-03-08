package com.simple.bets.modular.sys.service;

import com.simple.bets.core.common.util.Page;
import com.simple.bets.modular.sys.model.RoleModel;
import com.simple.bets.modular.sys.model.UserModel;
import com.simple.bets.modular.sys.model.UserRoleModel;
import com.simple.bets.core.base.service.IService;

public interface UserRoleService extends IService<UserRoleModel> {

	/**
	 * 分页 查询角色对应的用户
	 * @param page
	 * @param user
	 * @param exist true 存在 false 不存在
	 * @return
	 */
	Page<UserModel> queryPage(Page<UserModel> page, UserModel user, Boolean exist);

	/**
	 * 授权用户 角色
	 * @param role
	 */
	void assignRoleUser(RoleModel role);

	/**
	 * 删除该角色对应的所有用户
	 * @param roleIds
	 */

	void deleteAllUserRolesByRoleId(String roleIds);

	/**
	 * 通过角色删除用户授权
	 * @param role
	 */
	void deleteUserRolesByRoleId(RoleModel role);

	void deleteUserRolesByUserId(String userIds);
}
