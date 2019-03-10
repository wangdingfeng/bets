package com.simple.bets.core.common.util;

/**
 * 项目中常量
 */
public class BetsConstant {

    private BetsConstant() {

    }

    //用户登录次数计数  redisKey 前缀
    public static final String SHIRO_LOGIN_COUNT = "shiro_login_count_";

    //用户登录是否被锁定    一小时 redisKey 前缀
    public static final String SHIRO_IS_LOCK = "shiro_is_lock_";
    //用户初始密码
    public static final String RESET_PASSWORD = "123456";

    static final String XLSX_SUFFIX = ".xlsx";

    static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

}
