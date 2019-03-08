package com.simple.bets.modular.sys.service;

import com.simple.bets.modular.sys.model.UserModel;
import com.simple.bets.modular.sys.model.UserWithRoleDTO;
import com.simple.bets.core.base.service.IService;

/**
 * 用户信息Service
 * @author wangdingfeng
 * @Date 2019-01-07
 */
public interface UserService extends IService<UserModel> {
    /**
     * 查询用户所具有的角色
     * @param userId 用户id
     * @return
     */
    UserWithRoleDTO findUserRoleById(Long userId);

    /**
     * 根据用户名查询
     * @param userName
     * @return
     */
    UserModel findByName(String userName);

    /**
     * 保存或者更新数据
     * @param user
     * @return
     */
    UserModel saveOrUpdateUser(UserModel user);

    void updateLoginTime(String userName);

    void updatePassword(String password);

    UserModel findUserProfile(UserModel user);

    void updateUserProfile(UserModel user);
}
