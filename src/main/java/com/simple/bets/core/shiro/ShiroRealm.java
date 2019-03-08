package com.simple.bets.core.shiro;

import com.simple.bets.modular.sys.model.MenuModel;
import com.simple.bets.modular.sys.model.RoleModel;
import com.simple.bets.modular.sys.model.UserModel;
import com.simple.bets.modular.sys.service.MenuService;
import com.simple.bets.modular.sys.service.RoleService;
import com.simple.bets.modular.sys.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 自定义实现 ShiroRealm，包含认证和授权两大模块
 *
 * @author wangdingfeng
 */
@Component("shiroRealm")
public class ShiroRealm extends AuthorizingRealm {
    private Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //用户登录次数计数  redisKey 前缀
    private String SHIRO_LOGIN_COUNT = "shiro_login_count_";

    //用户登录是否被锁定    一小时 redisKey 前缀
    private String SHIRO_IS_LOCK = "shiro_is_lock_";

    /**
     * 授权模块，获取用户角色和权限
     *
     * @param principal principal
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        UserModel user = (UserModel) SecurityUtils.getSubject().getPrincipal();
        String userName = user.getUsername();

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        // 获取用户角色集
        List<RoleModel> roleList = this.roleService.findUserRole(userName);
        Set<String> roleSet = roleList.stream().map(RoleModel::getRoleName).collect(Collectors.toSet());
        simpleAuthorizationInfo.setRoles(roleSet);

        // 获取用户权限集
        List<MenuModel> permissionList = this.menuService.findUserPermissions(userName);
        Set<String> permissionSet = permissionList.stream().map(MenuModel::getPerms).collect(Collectors.toSet());
        simpleAuthorizationInfo.setStringPermissions(permissionSet);
        return simpleAuthorizationInfo;
    }

    /**
     * 用户认证
     *
     * @param token AuthenticationToken 身份认证 token
     * @return AuthenticationInfo 身份认证信息
     * @throws AuthenticationException 认证相关异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        // 获取用户输入的用户名和密码
        String userName = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());

        //访问一次，计数一次
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        opsForValue.increment(SHIRO_LOGIN_COUNT+userName, 1);
        //计数大于5时，设置用户被锁定一小时
        if(Integer.parseInt(opsForValue.get(SHIRO_LOGIN_COUNT+userName))>=5){
            opsForValue.set(SHIRO_IS_LOCK+userName, "LOCK");
            stringRedisTemplate.expire(SHIRO_IS_LOCK+userName, 1, TimeUnit.HOURS);
        }
        if ("LOCK".equals(opsForValue.get(SHIRO_IS_LOCK+userName))){
            logger.info("由于密码输入错误次数大于5次，帐号已经禁止登录,请一个小时后重试！");
            throw new DisabledAccountException("由于密码输入错误次数大于5次，帐号已经禁止登录,请一个小时后重试！请联系管理员");
        }

        // 通过用户名到数据库查询用户信息
        UserModel user = this.userService.findByName(userName);

        if (user == null)
            throw new UnknownAccountException("用户名或密码错误！");
        if (!password.equals(user.getPassword()))
            throw new IncorrectCredentialsException("用户名或密码错误！");
        if (UserModel.STATUS_LOCK.equals(user.getUserStatus()))
            throw new LockedAccountException("账号已被锁定,请联系管理员！");
        //清空登录计数
        opsForValue.set(SHIRO_LOGIN_COUNT+userName, "0");
        return new SimpleAuthenticationInfo(user, password, getName());
    }

    /**
     * 清除权限缓存
     * 使用方法：在需要清除用户权限的地方注入 ShiroRealm,
     * 然后调用其clearCache方法。
     */
    public void clearCache() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }

}
