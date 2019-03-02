package com.simple.bets.modular.sys.dao;


import com.simple.bets.modular.sys.model.Role;
import com.simple.bets.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @Author wangdingfeng
 * @Description //角色信息
 * @Date 9:58 2019/1/17
 **/
@Repository
public interface RoleMapper extends BaseMapper<Role> {
	
	List<Role> findUserRole(String userName);
}