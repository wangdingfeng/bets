package com.simple.bets.modular.sys.dao;

import com.simple.bets.core.base.mapper.BaseMapper;
import com.simple.bets.modular.sys.model.UserWithRole;
import com.simple.bets.modular.sys.model.User;

import java.util.List;
public interface UserMapper extends BaseMapper<User> {

	List<User> findUserWithDept(User user);
	
	List<UserWithRole> findUserWithRole(Long userId);
	
	User findUserProfile(User user);
}