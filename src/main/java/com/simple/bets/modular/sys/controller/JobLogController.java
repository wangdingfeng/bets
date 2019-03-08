package com.simple.bets.modular.sys.controller;

import com.simple.bets.core.annotation.Log;
import com.simple.bets.core.common.util.Page;
import com.simple.bets.core.base.controller.BaseController;
import com.simple.bets.core.base.model.ResponseResult;
import com.simple.bets.modular.sys.model.JobLogModel;
import com.simple.bets.modular.sys.service.JobLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 调度日志
 */
@Controller
@RequestMapping("/sys/jobLog")
public class JobLogController extends BaseController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JobLogService jobLogService;

    @Log("获取调度日志信息")
    @RequestMapping("/list")
    @RequiresPermissions("jobLog:list")
    public String list() {
        return "job/job-log-list";
    }

    /**
     * 日志列表
     *
     * @param request
     * @param response
     * @param log
     * @return
     */
    @RequestMapping("/listData")
    @RequiresPermissions("job:list")
    @ResponseBody
    public Page<JobLogModel> listData(HttpServletRequest request, HttpServletResponse response, JobLogModel log) {
        return jobLogService.queryPage(new Page<JobLogModel>(request, response), log);
    }

    /**
     * 删除日志
     *
     * @param ids
     * @return
     */
    @Log("删除调度日志")
    @RequiresPermissions("jobLog:delete")
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseResult deleteJobLog(String ids) {
        try {
            this.jobLogService.deleteBatch(ids);
            return ResponseResult.ok("删除调度日志成功！");
        } catch (Exception e) {
            log.error("删除调度日志失败", e);
            return ResponseResult.error("删除调度日志失败，请联系网站管理员！");
        }
    }
}
