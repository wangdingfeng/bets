package com.simple.bets.modular.job.service;


import com.simple.bets.core.base.service.IService;
import com.simple.bets.modular.job.model.JobLogModel;

import java.util.List;
/**
 * @Author wangdingfeng
 * @Description //定时任务日志管理
 * @Date 15:20 2019/2/2
 **/
public interface JobLogService extends IService<JobLogModel> {

	List<JobLogModel> findAllJobLogs(JobLogModel jobLog);

	void saveJobLog(JobLogModel log);
	
	void deleteBatch(String jobLogIds);
}
