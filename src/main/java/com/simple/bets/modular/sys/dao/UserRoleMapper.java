package com.simple.bets.modular.sys.dao;


import com.simple.bets.modular.sys.model.User;
import com.simple.bets.modular.sys.model.UserRole;
import com.simple.bets.core.base.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 角色对应用户
 */
@Component
public interface UserRoleMapper extends BaseMapper<UserRole> {
    /**
     * 查询角色对应的所有用户
     * @param user
     * @return
     */
   List<User> findUsersByRole(User user);

    /**
     * 查询不在此角色下边的用户
     * @param user
     * @return
     */
    List<User> findNotUsersByRole(User user);
}