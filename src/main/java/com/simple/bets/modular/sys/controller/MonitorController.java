package com.simple.bets.modular.sys.controller;

import com.simple.bets.core.base.controller.BaseController;
import com.simple.bets.core.common.util.Page;
import com.simple.bets.core.base.model.ResponseResult;
import com.simple.bets.modular.sys.model.UserOnlineModel;
import com.simple.bets.core.annotation.Log;
import com.simple.bets.modular.sys.service.MonitorService;
import com.simple.bets.core.common.vo.server.ServerInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author wangdingfeng
 * @Description 系统监控
 * @Date 14:37 2019/2/2
 **/

@Controller
public class MonitorController extends BaseController {

    private static final String PAGE_SUFFIX = "modular/sys/monitor";

    @Autowired
    MonitorService monitorService;

    /**
     * 跳转在线用户列表
     *
     * @return
     */
    @Log("获取在线用户信息")
    @RequestMapping("/sys/session/list")
    @RequiresPermissions("session:list")
    public String list() {
        return PAGE_SUFFIX+"/user-online";
    }

    /**
     * list
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/sys/session/listData")
    @RequiresPermissions("session:list")
    public Page<UserOnlineModel> listData() {
        List<UserOnlineModel> list = monitorService.list();
        Page<UserOnlineModel> page = new Page<>();
        page.setList(list);
        return page;
    }

    /**
     * 踢出用户
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user:kickout")
    @RequestMapping("/sys/session/forceLogout")
    public ResponseResult forceLogout(String id) {
        try {
            monitorService.forceLogout(id);
            return ResponseResult.ok("操作成功");
        } catch (Exception e) {
            logger.error("踢出用户失败", e);
            return ResponseResult.error("踢出用户失败");
        }
    }

    /**
     * 服务器监控
     * @return
     */
    @GetMapping(value = "/sys/service/view")
    public String service(Model model) {
        ServerInfo server = monitorService.getServerInfo();
        model.addAttribute("server", server);
        return PAGE_SUFFIX+"/service-info";
    }

}
