package com.simple.bets.modular.sys.service;

import com.simple.bets.modular.sys.model.UserRole;
import com.simple.bets.core.service.IService;

public interface UserRoleService extends IService<UserRole> {

	void deleteUserRolesByRoleId(String roleIds);

	void deleteUserRolesByUserId(String userIds);
}
