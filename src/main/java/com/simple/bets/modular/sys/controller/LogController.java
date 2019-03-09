package com.simple.bets.modular.sys.controller;

import com.simple.bets.core.common.util.FileUtil;
import com.simple.bets.core.common.util.Page;
import com.simple.bets.core.base.controller.BaseController;
import com.simple.bets.core.base.model.ResponseResult;
import com.simple.bets.modular.sys.model.LogModel;
import com.simple.bets.modular.sys.service.LogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/sys/log")
public class LogController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String PAGE_SUFFIX = "modular/sys/log";

    @Autowired
    private LogService logService;

    /**
     * 跳转到系统日志页面
     *
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("log:list")
    public String list() {
        return PAGE_SUFFIX + "/log-list";
    }

    /**
     * 获取日志列表
     *
     * @return
     */
    @RequestMapping("/listData")
    @ResponseBody
    public Page<LogModel> listData(LogModel log, HttpServletRequest request, HttpServletResponse response) {
        return logService.queryPage(new Page<LogModel>(request, response), log);
    }

    /**
     * 删除日志数据
     * @param ids
     * @return
     */
    @RequiresPermissions("log:delete")
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseResult deleteLogss(String ids) {
        try {
            this.logService.deleteLogs(ids);
            return ResponseResult.ok("删除日志成功！");
        } catch (Exception e) {
            logger.error("删除日志失败", e);
            return ResponseResult.error("删除日志失败，请联系网站管理员！");
        }
    }
}
