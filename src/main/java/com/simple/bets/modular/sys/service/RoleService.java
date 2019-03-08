package com.simple.bets.modular.sys.service;


import com.simple.bets.modular.sys.model.RoleModel;
import com.simple.bets.core.base.service.IService;

import java.util.List;
/**
 * @Author wangdingfeng
 * @Description 角色管理
 * @Date 18:55 2019/1/23
 **/

public interface RoleService extends IService<RoleModel> {

	/**
	 * 查询用户角色信息
	 * @param userName
	 * @return
	 */
	List<RoleModel> findUserRole(String userName);

	/**
	 * 保存|修改
	 * @param role
	 * @return
	 */
	RoleModel saveOrUpdate(RoleModel role);

	List<RoleModel> findAllRole(RoleModel role);

	RoleModel findByName(String roleName);
	/**
	 * 批量删除角色
	 * @param roleIds
	 */
	void deleteRoles(String roleIds);
}
