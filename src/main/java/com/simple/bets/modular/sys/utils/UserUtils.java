package com.simple.bets.modular.sys.utils;

import com.simple.bets.core.common.util.SpringContextUtils;
import com.simple.bets.core.redis.JedisUtils;
import com.simple.bets.modular.sys.model.UserModel;
import com.simple.bets.modular.sys.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ProjectName: bets
 * @Package: com.simple.bets.modular.sys.utils
 * @ClassName: UserUtils
 * @Author: wangdingfeng
 * @Description: 用户工具类
 * @Date: 2019/3/9 15:32
 * @Version: 1.0
 */
public class UserUtils {

    private static Logger logger = LoggerFactory.getLogger(UserUtils.class);

    private static UserService userService = SpringContextUtils.getBean(UserService.class);
    /**
     * 获取授权主要对象
     */
    public static Subject getSubject(){
        return SecurityUtils.getSubject();
    }

    /**
     * 获取shiro中用户信息
     * @return
     */
    public static UserModel getPrincipal(){
        UserModel userModel = null;
        try {
            userModel = (UserModel) getSubject().getPrincipal();
        } catch (Exception e) {
            logger.error("无法获取shiro中用户登录信息");
        }
        return userModel ;
    }

    /**
     *通过用户账号获取用户信息
     * @param userName
     * @return
     */
    public static UserModel getUserInfo(String userName){
        UserModel userModel =(UserModel)JedisUtils.getObject(userName);
        if(null == userModel){
            userModel =  userService.findByName(userName);
            //保存1小时
            JedisUtils.setObject(userName,userModel,3600);
        }
        return userModel;
    }
}
