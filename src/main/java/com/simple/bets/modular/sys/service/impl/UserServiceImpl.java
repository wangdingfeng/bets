package com.simple.bets.modular.sys.service.impl;

import com.simple.bets.core.common.util.MD5Utils;
import com.simple.bets.modular.sys.dao.UserMapper;
import com.simple.bets.modular.sys.dao.UserRoleMapper;
import com.simple.bets.modular.sys.model.User;
import com.simple.bets.modular.sys.model.UserRole;
import com.simple.bets.modular.sys.model.UserWithRole;
import com.simple.bets.core.service.impl.ServiceImpl;
import com.simple.bets.modular.sys.service.UserRoleService;
import com.simple.bets.modular.sys.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户serviceImpl
 * @author wangdingfeng
 * @Date 2019-01-07
 */
@Service("userService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<User> implements UserService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public User findByName(String userName) {
        Example example = new Example(User.class);
        example.createCriteria().andCondition("lower(username)=", userName.toLowerCase());
        List<User> list = this.selectByExample(example);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public User saveOrUpdateUser(User user) {
        if(null == user.getUserId()) {
            saveUser(user);
            this.save(user);
        }else{
            this.updateNotNull(user);
        }
        return user;
    }

/*    @Override
    public List<User> findUserWithDept(User user, QueryRequest request) {
        try {
            return this.userMapper.findUserWithDept(user);
        } catch (Exception e) {
            log.error("error", e);
            return new ArrayList<>();
        }
    }*/
    @Override
    @Transactional
    public void registerUser(User user) {
        saveUser(user);
        user.setSex(User.SEX_UNKNOW);
        UserRole ur = new UserRole();
        ur.setUserId(user.getUserId());
        ur.setRoleId(3L);
        this.userRoleMapper.insert(ur);
    }


    @Override
    @Transactional
    public void addUser(User user, Long[] roles) {
        saveUser(user);
        setUserRoles(user, roles);
    }

    /**
     * 保存用户信息
     * @param user
     * @return
     */
    private User saveUser(User user){
        user.setCreateTime(new Date());
        user.setPassword(MD5Utils.encrypt(user.getUsername(), user.getPassword()));
        return user;
    }

    private void setUserRoles(User user, Long[] roles) {
        Arrays.stream(roles).forEach(roleId -> {
            UserRole ur = new UserRole();
            ur.setUserId(user.getUserId());
            ur.setRoleId(roleId);
            this.userRoleMapper.insert(ur);
        });
    }

    @Override
    @Transactional
    public void updateUser(User user, Long[] roles) {
        user.setPassword(null);
        user.setUsername(null);
        user.setModifyTime(new Date());
        this.updateNotNull(user);
        Example example = new Example(UserRole.class);
        example.createCriteria().andCondition("user_id=", user.getUserId());
        this.userRoleMapper.deleteByExample(example);
        setUserRoles(user, roles);
    }

    @Override
    @Transactional
    public void updateLoginTime(String userName) {
        Example example = new Example(User.class);
        example.createCriteria().andCondition("lower(username)=", userName.toLowerCase());
        User user = new User();
        user.setLastLoginTime(new Date());
        this.userMapper.updateByExampleSelective(user, example);
    }

    @Override
    @Transactional
    public void updatePassword(String password) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Example example = new Example(User.class);
        example.createCriteria().andCondition("username=", user.getUsername());
        String newPassword = MD5Utils.encrypt(user.getUsername().toLowerCase(), password);
        user.setPassword(newPassword);
        this.userMapper.updateByExampleSelective(user, example);
    }

    @Override
    public UserWithRole findUserRoleById(Long userId) {
        List<UserWithRole> list = this.userMapper.findUserWithRole(userId);
        List<Long> roleList = list.stream().map(UserWithRole::getRoleId).collect(Collectors.toList());
        if (list.isEmpty())
            return null;
        UserWithRole userWithRole = list.get(0);
        userWithRole.setRoleIds(roleList);
        return userWithRole;
    }

    @Override
    public User findUserProfile(User user) {
        return this.userMapper.findUserProfile(user);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateUserProfile(User user) {
        user.setUsername(null);
        user.setPassword(null);
        this.updateNotNull(user);
    }

}
