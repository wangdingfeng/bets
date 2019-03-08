package com.simple.bets.core.base.controller;

import com.simple.bets.modular.sys.model.UserModel;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基础Controller
 */
public class BaseController {


    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取shiro中用户
     * @return
     */
    protected static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 获取用户
     * @return
     */
    protected UserModel getCurrentUser() {
        return (UserModel) getSubject().getPrincipal();
    }

    /**
     * 获取 Session
     * @return
     */
    protected Session getSession() {
        return getSubject().getSession();
    }

    protected Session getSession(Boolean flag) {
        return getSubject().getSession(flag);
    }

    protected void login(AuthenticationToken token) {
        getSubject().login(token);
    }
}
