package com.simple.bets.modular.sys.service;

import com.simple.bets.modular.sys.model.UserModel;
import com.simple.bets.modular.sys.model.UserWithRoleDTO;
import com.simple.bets.core.base.service.IService;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * 更新登录信息
     * @param userName
     * @param request
     */
    void updateLoginInfo(String userName, HttpServletRequest request);

    /**
     * 修改密码
     * @param password
     */
    void updatePassword(String oldPassword,String password);

    /**
     * 查询用户角色
     * @param user
     * @return
     */
    UserModel findUserProfile(UserModel user);

    /**
     * 更新用户基本信息
     * @param user
     */
    void updateUserProfile(UserModel user);

    /**
     * 更新用户状态
     * @param userModel
     */
    void updateUserStatus(UserModel userModel);

    /**
     * 重置用户初始密码
     * @param userMode
     */
    void resetPassword(UserModel userMode);
}
