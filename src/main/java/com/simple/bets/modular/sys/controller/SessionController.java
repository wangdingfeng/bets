package com.simple.bets.modular.sys.controller;

import com.simple.bets.core.common.util.Page;
import com.simple.bets.core.model.ResponseResult;
import com.simple.bets.modular.sys.model.UserOnline;
import com.simple.bets.core.annotation.Log;
import com.simple.bets.modular.sys.service.SessionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author wangdingfeng
 * @Description session 用户管理
 * @Date 14:37 2019/2/2
 **/

@Controller
public class SessionController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SessionService sessionService;

    /**
     * 跳转在线用户列表
     * @return
     */
    @Log("获取在线用户信息")
    @RequestMapping("session")
    @RequiresPermissions("session:list")
    public String online() {
        return "sys/user/user-online";
    }

    /**
     * list
     * @return
     */
    @ResponseBody
    @RequestMapping("session/list")
    @RequiresPermissions("session:list")
    public Page<UserOnline> list() {
        List<UserOnline> list = sessionService.list();
        Page<UserOnline> page = new Page<>();
        page.setList(list);
        return page;
    }

    /**
     * 踢出用户
     * @param id
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user:kickout")
    @RequestMapping("session/forceLogout")
    public ResponseResult forceLogout(String id) {
        try {
            sessionService.forceLogout(id);
            return ResponseResult.ok();
        } catch (Exception e) {
            log.error("踢出用户失败", e);
            return ResponseResult.error("踢出用户失败");
        }

    }
}
