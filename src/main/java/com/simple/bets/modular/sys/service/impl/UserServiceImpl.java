package com.simple.bets.modular.sys.service.impl;

import com.simple.bets.core.common.exception.ServiceException;
import com.simple.bets.core.common.lang.StringUtils;
import com.simple.bets.core.common.util.BetsConstant;
import com.simple.bets.core.common.util.IPUtils;
import com.simple.bets.core.common.util.MD5Utils;
import com.simple.bets.modular.sys.dao.UserMapper;
import com.simple.bets.modular.sys.model.UserModel;
import com.simple.bets.modular.sys.model.UserWithRoleDTO;
import com.simple.bets.core.base.service.impl.ServiceImpl;
import com.simple.bets.modular.sys.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
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
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public UserModel findByName(String userName) {
        UserModel query = new UserModel();
        query.setUsername(userName);
        return super.findByModel(query);
    }

    @Override
    @Transactional
    public UserModel saveOrUpdateUser(UserModel user) {
        if (null == user.getUserId()) {
            saveUser(user);
        }
        super.merge(user);
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

    @Override
    @Transactional
    public void updateLoginInfo(String userName, HttpServletRequest request) {
        Example example = new Example(UserModel.class);
        example.createCriteria().andCondition("lower(username)=", userName.toLowerCase());
        UserModel updateUser = new UserModel();
        updateUser.setLoginIp(IPUtils.getIpAddr(request));
        updateUser.setLastLoginTime(new Date());
        this.userMapper.updateByExampleSelective(updateUser, example);
    }

    @Override
    @Transactional
    public void updatePassword(String oldPassword,String newPassword) {
        UserModel user = (UserModel) SecurityUtils.getSubject().getPrincipal();
        String newPasswordMD5 = MD5Utils.encryptBasedDes(user.getUsername().toLowerCase() + newPassword);
        //校验密码是否正确
        if(!MD5Utils.decryptBasedDes(user.getPassword()).equals(user.getUsername().toLowerCase()+oldPassword)){
            throw new ServiceException("原始密码不正确");
        }
        //校验新密码和旧密码是否相同
        if(newPasswordMD5.equals(user.getPassword())){
            throw new ServiceException("新密码和旧密码相同");
        }
        Example example = new Example(UserModel.class);
        example.createCriteria().andCondition("username=", user.getUsername());
        user.setPassword(newPasswordMD5);
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
        this.update(user);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateUserStatus(UserModel userModel) {
        super.update(userModel);
    }

    @Override
    public void resetPassword(UserModel userModel) {
        //判断用户是否被锁
        if(stringRedisTemplate.hasKey(BetsConstant.SHIRO_IS_LOCK+userModel.getUsername())){
            stringRedisTemplate.delete(BetsConstant.SHIRO_LOGIN_COUNT+userModel.getUsername());
            stringRedisTemplate.delete(BetsConstant.SHIRO_IS_LOCK+userModel.getUsername());
        }
        userModel.setPassword(MD5Utils.encryptBasedDes(userModel.getUsername().toLowerCase() + BetsConstant.RESET_PASSWORD));
        super.update(userModel);
    }

}
