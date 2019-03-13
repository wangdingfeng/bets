package com.simple.bets.core.base.controller;

import com.simple.bets.core.common.lang.DateUtils;
import com.simple.bets.modular.sys.model.UserModel;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 基础Controller 提供当前登录用户信息
 */
public abstract  class BaseController {


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

    /**
     * 处理前台接收的时间类型
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                return value != null ? value.toString() : "";
            }
        });

        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });

        // Timestamp 类型转换
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                Date date = DateUtils.parseDate(text);
                setValue(date == null ? null : new Timestamp(date.getTime()));
            }
        });
    }
}
