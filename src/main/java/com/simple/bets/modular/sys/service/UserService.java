package com.simple.bets.modular.sys.service;

import com.simple.bets.modular.sys.model.User;
import com.simple.bets.modular.sys.model.UserWithRole;
import com.simple.bets.core.service.IService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;

/**
 * 用户信息Service
 * @author wangdingfeng
 * @Date 2019-01-07
 */
@CacheConfig(cacheNames = "UserService")
public interface UserService extends IService<User> {
    /**
     * 查询用户所具有的角色
     * @param userId 用户id
     * @return
     */
    UserWithRole findUserRoleById(Long userId);

    /**
     * 根据用户名查询
     * @param userName
     * @return
     */
    User findByName(String userName);

    /**
     * 保存或者更新数据
     * @param user
     * @return
     */
    User saveOrUpdateUser(User user);

    @CacheEvict(key = "#p0", allEntries = true)
    void registerUser(User user);

    @CacheEvict(allEntries = true)
    void addUser(User user, Long[] roles);

    @CacheEvict(key = "#p0", allEntries = true)
    void updateUser(User user, Long[] roles);

    void updateLoginTime(String userName);

    void updatePassword(String password);

    User findUserProfile(User user);

    void updateUserProfile(User user);
}
