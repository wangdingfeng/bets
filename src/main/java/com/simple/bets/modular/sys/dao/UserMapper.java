package com.simple.bets.modular.sys.dao;

import com.simple.bets.core.base.mapper.BaseMapper;
import com.simple.bets.modular.sys.model.UserWithRoleDTO;
import com.simple.bets.modular.sys.model.UserModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户基本信息
 */
@Repository
public interface UserMapper extends BaseMapper<UserModel> {

	List<UserModel> findUserWithDept(UserModel user);
	
	List<UserWithRoleDTO> findUserWithRole(Long userId);
	
	UserModel findUserProfile(UserModel user);
}