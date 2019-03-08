package com.simple.bets.modular.sys.dao;


import com.simple.bets.core.base.mapper.BaseMapper;
import com.simple.bets.modular.sys.model.UserModel;
import com.simple.bets.modular.sys.model.UserRoleModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色对应用户
 */
@Repository
public interface UserRoleMapper extends BaseMapper<UserRoleModel> {
    /**
     * 查询角色对应的所有用户
     * @param user
     * @return
     */
   List<UserModel> findUsersByRole(UserModel user);

    /**
     * 查询不在此角色下边的用户
     * @param user
     * @return
     */
    List<UserModel> findNotUsersByRole(UserModel user);
}