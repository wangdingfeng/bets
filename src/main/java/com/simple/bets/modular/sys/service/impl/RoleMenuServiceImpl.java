package com.simple.bets.modular.sys.service.impl;

import com.simple.bets.modular.sys.model.RoleMenu;
import com.simple.bets.modular.sys.service.RoleMenuService;
import com.simple.bets.core.base.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("roleMenuService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenu> implements RoleMenuService {

	@Override
	public List<Long> findRoleMenu(Long roleId) {
		RoleMenu roleMenuPrams = new RoleMenu();
		roleMenuPrams.setRoleId(roleId);
		List<RoleMenu> roleMenuList = super.queryObjectForList(roleMenuPrams);
		List<Long> menuIds = roleMenuList.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
		return menuIds;
	}

	@Override
	@Transactional
	public void deleteRoleMenusByRoleId(String roleIds) {
		List<String> list = Arrays.asList(roleIds.split(","));
		this.batchDelete(list, "roleId", RoleMenu.class);
	}

	@Override
	@Transactional
	public void deleteRoleMenusByMenuId(String menuIds) {
		List<String> list = Arrays.asList(menuIds.split(","));
		this.batchDelete(list, "menuId", RoleMenu.class);
	}

}
