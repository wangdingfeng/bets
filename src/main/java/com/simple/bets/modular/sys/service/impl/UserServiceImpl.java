package com.simple.bets.modular.sys.service.impl;

import com.simple.bets.core.common.util.MD5Utils;
import com.simple.bets.modular.sys.dao.UserMapper;
import com.simple.bets.modular.sys.dao.UserRoleMapper;
import com.simple.bets.modular.sys.model.UserModel;
import com.simple.bets.modular.sys.model.UserRoleModel;
import com.simple.bets.modular.sys.model.UserWithRoleDTO;
import com.simple.bets.core.base.service.impl.ServiceImpl;
import com.simple.bets.modular.sys.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户serviceImpl
 *
 * @author wangdingfeng
 * @Date 2019-01-07
 */
@Service("userService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserModel> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public UserModel findByName(String userName) {
        Example example = new Example(UserModel.class);
        example.createCriteria().andCondition("lower(username)=", userName.toLowerCase());
        List<UserModel> list = this.selectByExample(example);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    @Transactional
    public UserModel saveOrUpdateUser(UserModel user) {
        if (null == user.getUserId()) {
            saveUser(user);
            user.setBaseData(true);
            this.save(user);
        } else {
            user.setBaseData(false);
            this.updateNotNull(user);
        }
        return user;
    }

    /**
     * 保存用户信息
     *
     * @param user
     * @return
     */
    private UserModel saveUser(UserModel user) {
        user.setPassword(MD5Utils.encryptBasedDes(user.getUsername() + user.getPassword()));
        user.setUserStatus(UserModel.STATUS_VALID);
        return user;
    }

    private void setUserRoles(UserModel user, Long[] roles) {
        Arrays.stream(roles).forEach(roleId -> {
            UserRoleModel ur = new UserRoleModel();
            ur.setUserId(user.getUserId());
            ur.setRoleId(roleId);
            this.userRoleMapper.insert(ur);
        });
    }

    @Override
    @Transactional
    public void updateLoginTime(String userName) {
        Example example = new Example(UserModel.class);
        example.createCriteria().andCondition("lower(username)=", userName.toLowerCase());
        UserModel user = new UserModel();
        user.setLastLoginTime(new Date());
        this.userMapper.updateByExampleSelective(user, example);
    }

    @Override
    @Transactional
    public void updatePassword(String password) {
        UserModel user = (UserModel) SecurityUtils.getSubject().getPrincipal();
        Example example = new Example(UserModel.class);
        example.createCriteria().andCondition("username=", user.getUsername());
        String newPassword = MD5Utils.encryptBasedDes(user.getUsername().toLowerCase() + password);
        user.setPassword(newPassword);
        this.userMapper.updateByExampleSelective(user, example);
    }

    @Override
    public UserWithRoleDTO findUserRoleById(Long userId) {
        List<UserWithRoleDTO> list = this.userMapper.findUserWithRole(userId);
        List<Long> roleList = list.stream().map(UserWithRoleDTO::getRoleId).collect(Collectors.toList());
        if (list.isEmpty())
            return null;
        UserWithRoleDTO userWithRole = list.get(0);
        userWithRole.setRoleIds(roleList);
        return userWithRole;
    }

    @Override
    public UserModel findUserProfile(UserModel user) {
        return this.userMapper.findUserProfile(user);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateUserProfile(UserModel user) {
        user.setUsername(null);
        user.setPassword(null);
        this.updateNotNull(user);
    }

}
