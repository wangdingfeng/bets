package com.simple.bets.modular.sys.service.impl;

import com.simple.bets.modular.sys.model.RoleMenuModel;
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
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuModel> implements RoleMenuService {

	@Override
	public List<Long> findRoleMenu(Long roleId) {
		RoleMenuModel roleMenuPrams = new RoleMenuModel();
		roleMenuPrams.setRoleId(roleId);
		List<RoleMenuModel> roleMenuList = super.queryObjectForList(roleMenuPrams);
		List<Long> menuIds = roleMenuList.stream().map(RoleMenuModel::getMenuId).collect(Collectors.toList());
		return menuIds;
	}

	@Override
	@Transactional
	public void deleteRoleMenusByRoleId(String roleIds) {
		List<String> list = Arrays.asList(roleIds.split(","));
		this.batchDelete(list, "roleId", RoleMenuModel.class);
	}

	@Override
	@Transactional
	public void deleteRoleMenusByMenuId(String menuIds) {
		List<String> list = Arrays.asList(menuIds.split(","));
		this.batchDelete(list, "menuId", RoleMenuModel.class);
	}

}
