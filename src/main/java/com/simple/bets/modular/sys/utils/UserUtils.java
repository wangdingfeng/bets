package com.simple.bets.modular.sys.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

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
    /**
     * 获取授权主要对象
     */
    public static Subject getSubject(){
        return SecurityUtils.getSubject();
    }
}
