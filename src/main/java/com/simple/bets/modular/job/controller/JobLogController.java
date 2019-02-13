package com.simple.bets.modular.job.controller;

import com.simple.bets.core.annotation.Log;
import com.simple.bets.core.common.util.Page;
import com.simple.bets.core.controller.BaseController;
import com.simple.bets.core.model.ResponseResult;
import com.simple.bets.modular.job.model.JobLogModel;
import com.simple.bets.modular.job.service.JobLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("jobLog")
public class JobLogController extends BaseController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JobLogService jobLogService;

	@Log("获取调度日志信息")
	@RequestMapping("/index")
//	@RequiresPermissions("jobLog:list")
	public String index() {
		return "job/job-log-list";
	}

	@RequestMapping("/list")
	@RequiresPermissions("job:list")
	@ResponseBody
	public Page<JobLogModel> jobLogList(HttpServletRequest request, HttpServletResponse response, JobLogModel log) {
		return jobLogService.queryPage(new Page<JobLogModel>(request,response),log);
	}

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
