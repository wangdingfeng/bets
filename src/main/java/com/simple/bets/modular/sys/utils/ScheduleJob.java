package com.simple.bets.modular.sys.utils;

import com.simple.bets.core.common.util.SpringContextUtils;
import com.simple.bets.modular.sys.model.JobLogModel;
import com.simple.bets.modular.sys.model.JobModel;
import com.simple.bets.modular.sys.service.JobLogService;
import com.simple.bets.modular.sys.service.JobService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 定时任务
 *
 * @author wangdingfeng
 */
public class ScheduleJob extends QuartzJobBean {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ExecutorService service = Executors.newSingleThreadExecutor();

    @Override
    protected void executeInternal(JobExecutionContext context) {
        Long jobId =(Long) context.getMergedJobDataMap().get(JobModel.JOB_PARAM_KEY);
        //查询详情数据
        JobService scheduleJobService = (JobService) SpringContextUtils.getBean("jobService");
        JobModel scheduleJob = scheduleJobService.findById(jobId);
        // 获取spring bean
        JobLogService scheduleJobLogService = (JobLogService) SpringContextUtils.getBean("jobLogService");

        JobLogModel log = new JobLogModel();
        log.setJobId(scheduleJob.getJobId());
        log.setBeanName(scheduleJob.getBeanName());
        log.setMethodName(scheduleJob.getMethodName());
        log.setParams(scheduleJob.getParams());
        log.setCreateTime(new Date());
        long startTime = System.currentTimeMillis();
        try {
            // 执行任务
            logger.info("任务准备执行，任务ID：{}", scheduleJob.getJobId());
            ScheduleRunnable task = new ScheduleRunnable(scheduleJob.getBeanName(), scheduleJob.getMethodName(),
                    scheduleJob.getParams());
            Future<?> future = service.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            log.setTimes(times);
            // 任务状态 0：成功 1：失败
            log.setStatus("0");
            scheduleJob.setLastTime(context.getFireTime());
            scheduleJob.setNextTime(context.getNextFireTime());
            scheduleJobService.update(scheduleJob);
            logger.info("任务执行完毕，任务ID：{} 总共耗时：{} 毫秒", scheduleJob.getJobId(), times);
        } catch (Exception e) {
            logger.error("任务执行失败，任务ID：" + scheduleJob.getJobId(), e);
            long times = System.currentTimeMillis() - startTime;
            log.setTimes(times);
            // 任务状态 0：成功 1：失败
            log.setStatus("1");
            log.setError(StringUtils.substring(e.toString(), 0, 2000));
        } finally {
            scheduleJobLogService.saveJobLog(log);
        }
    }
}
