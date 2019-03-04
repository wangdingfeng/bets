package com.simple.bets.modular.sys.service;


import com.simple.bets.modular.sys.model.RoleMenu;
import com.simple.bets.core.base.service.IService;

import java.util.List;

/**
 * @Author wangdingfeng
 * @Description //角色菜单管理
 * @Date 9:27 2019/1/24
 **/

public interface RoleMenuService extends IService<RoleMenu> {
	/**
	 * 获取角色菜单
	 * @param roleId
	 * @return
	 */
	List<Long> findRoleMenu(Long roleId);

	/**
	 * 通过角色id批量删除角色菜单
	 * @param roleIds 角色id
	 */
	void deleteRoleMenusByRoleId(String roleIds);

	/**
	 * 通过菜单id批量删除角色菜单
	 * @param menuIds 菜单id
	 */
	void deleteRoleMenusByMenuId(String menuIds);
}
