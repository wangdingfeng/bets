package com.simple.bets.modular.job.controller;

import com.simple.bets.core.annotation.Log;
import com.simple.bets.core.common.util.Page;
import com.simple.bets.core.base.controller.BaseController;
import com.simple.bets.core.base.model.ResponseResult;
import com.simple.bets.modular.job.model.JobModel;
import com.simple.bets.modular.job.service.JobService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("job")
public class JobController extends BaseController {


    @Autowired
    private JobService jobService;

    /**
     * 定时任务列表页
     * @return
     */
    @Log("获取定时任务信息")
    @RequestMapping("/index")
    @RequiresPermissions("job:list")
    public String index() {
        return "job/job-list";
    }

    /**
     * 定时任务数据
     * @param request
     * @param response
     * @param job
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("job:list")
    @ResponseBody
    public Page<JobModel> list(HttpServletRequest request, HttpServletResponse response, JobModel job) {
        return jobService.queryPage(new Page<JobModel>(request,response),job);
    }

    /**
     * 编辑详情
     * @param jobModel
     * @param model
     * @return
     */
    @RequestMapping("/form")
    public String form(JobModel jobModel, Model model) {
        if(null != jobModel.getJobId()){
            jobModel = jobService.findJob(jobModel.getJobId());
        }
        model.addAttribute("job",jobModel);
        return "job/job-form";
    }

    /**
     * 检验 时间戳
     * @param cron
     * @return
     */
    @RequestMapping("/checkCron")
    @ResponseBody
    public boolean checkCron(String cron) {
        try {
            return CronExpression.isValidExpression(cron);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 编辑任务
     * @param job
     * @return
     */
    @Log("编辑任务 ")
    @RequestMapping("/saveOrUpdate")
    @RequiresPermissions("job:add")
    @ResponseBody
    public ResponseResult saveOrUpdate(JobModel job){
        try {
            this.jobService.saveOrUpdate(job);
            return ResponseResult.ok("保存任务成功！");
        } catch (Exception e) {
            logger.error("保存任务成功失败", e);
            return ResponseResult.error("保存任务失败，请联系网站管理员！");
        }
    }

    /**
     * 删除任务
     * @param ids
     * @return
     */
    @Log("删除任务")
    @RequiresPermissions("job:delete")
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseResult deleteJob(String ids) {
        try {
            this.jobService.deleteBatch(ids);
            return ResponseResult.ok("删除任务成功！");
        } catch (Exception e) {
            logger.error("删除任务失败", e);
            return ResponseResult.error("删除任务失败，请联系网站管理员！");
        }
    }

    /**
     * 执行任务
     * @param jobIds
     * @return
     */
    @Log("执行任务")
    @RequiresPermissions("job:run")
    @RequestMapping("job/run")
    @ResponseBody
    public ResponseResult runJob(String jobIds) {
        try {
            this.jobService.run(jobIds);
            return ResponseResult.ok("执行任务成功！");
        } catch (Exception e) {
            logger.error("执行任务失败", e);
            return ResponseResult.error("执行任务失败，请联系网站管理员！");
        }
    }

    /**
     * 暂停任务
     * @param jobIds
     * @return
     */
    @Log("暂停任务")
    @RequiresPermissions("job:pause")
    @RequestMapping("/pause")
    @ResponseBody
    public ResponseResult pauseJob(String jobIds) {
        try {
            this.jobService.pause(jobIds);
            return ResponseResult.ok("暂停任务成功！");
        } catch (Exception e) {
            logger.error("暂停任务失败", e);
            return ResponseResult.error("暂停任务失败，请联系网站管理员！");
        }
    }

    /**
     * 恢复任务
     * @param jobIds
     * @return
     */
    @Log("恢复任务")
    @RequiresPermissions("job:resume")
    @RequestMapping("/resume")
    @ResponseBody
    public ResponseResult resumeJob(String jobIds) {
        try {
            this.jobService.resume(jobIds);
            return ResponseResult.ok("恢复任务成功！");
        } catch (Exception e) {
            logger.error("恢复任务失败", e);
            return ResponseResult.error("恢复任务失败，请联系网站管理员！");
        }
    }

    /**
     * @param job 定时任务
     * @return ResponseBo
     */
    @RequestMapping("/getSysCronClazz")
    @ResponseBody
    public ResponseResult getSysCronClazz(JobModel job) {
        List<JobModel> sysCronClazz = this.jobService.getSysCronClazz(job);
        return ResponseResult.ok(sysCronClazz);
    }
}
