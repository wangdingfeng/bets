package com.simple.bets.modular.sys.service.impl;

import com.simple.bets.core.annotation.CronTag;
import com.simple.bets.core.base.service.impl.ServiceImpl;
import com.simple.bets.modular.sys.dao.JobMapper;
import com.simple.bets.modular.sys.model.JobModel;
import com.simple.bets.modular.sys.service.JobService;
import com.simple.bets.modular.sys.utils.ScheduleUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

@Service("jobService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JobServiceImpl extends ServiceImpl<JobModel> implements JobService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private JobMapper jobMapper;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() {
        List<JobModel> scheduleJobList = this.jobMapper.findAllRunJobList();
        // 如果不存在，则创建
        scheduleJobList.forEach(scheduleJob -> {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getJobId());
            if (cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            } else {
                ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
            }
        });
    }

    @Override
    public JobModel findJob(Long jobId) {
        return this.selectByKey(jobId);
    }

    @Override
    public List<JobModel> findAllJobs(JobModel job) {
        try {
            Example example = new Example(JobModel.class);
            Criteria criteria = example.createCriteria();
            criteria.andAllEqualTo(job);
            example.setOrderByClause("job_id");
            return this.selectByExample(example);
        } catch (Exception e) {
            logger.error("获取任务失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public void saveOrUpdate(JobModel jobModel) {
        if(null == jobModel.getJobId()){
            jobModel.setBaseData(true);
            jobModel.setJobStatus(JobModel.ScheduleStatus.NORMAL.getValue());
            super.save(jobModel);
            ScheduleUtils.createScheduleJob(scheduler, jobModel);
        }else{
            jobModel.setBaseData(false);
            ScheduleUtils.updateScheduleJob(scheduler, jobModel);
            super.updateNotNull(jobModel);
        }
    }

    @Override
    @Transactional
    public void deleteBatch(String jobIds) {
        List<String> list = Arrays.asList(jobIds.split(","));
        list.forEach(jobId -> ScheduleUtils.deleteScheduleJob(scheduler, Long.valueOf(jobId)));
        this.batchDelete(list, "jobId", JobModel.class);
    }

    @Override
    @Transactional
    public int updateBatch(String jobIds, String status) {
        List<String> list = Arrays.asList(jobIds.split(","));
        Example example = new Example(JobModel.class);
        example.createCriteria().andIn("jobId", list);
        JobModel job = new JobModel();
        job.setJobStatus(status);
        return this.jobMapper.updateByExampleSelective(job, example);
    }

    @Override
    @Transactional
    public void run(String jobIds) {
        String[] list = jobIds.split(",");
        Arrays.stream(list).forEach(jobId -> ScheduleUtils.run(scheduler, this.findJob(Long.valueOf(jobId))));
    }

    @Override
    @Transactional
    public void pause(String jobIds) {
        String[] list = jobIds.split(",");
        Arrays.stream(list).forEach(jobId -> ScheduleUtils.pauseJob(scheduler, Long.valueOf(jobId)));
        this.updateBatch(jobIds, JobModel.ScheduleStatus.PAUSE.getValue());
    }

    @Override
    @Transactional
    public void resume(String jobIds) {
        String[] list = jobIds.split(",");
        Arrays.stream(list).forEach(jobId -> ScheduleUtils.resumeJob(scheduler, Long.valueOf(jobId)));
        this.updateBatch(jobIds, JobModel.ScheduleStatus.NORMAL.getValue());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<JobModel> getSysCronClazz(JobModel job) {
        Reflections reflections = new Reflections("com.simple.bets.modular.sys.task");
        List<JobModel> jobList = new ArrayList<>();
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(CronTag.class);

        for (Class cls : annotated) {
            String clsSimpleName = cls.getSimpleName();
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                JobModel job1 = new JobModel();
                String methodName = method.getName();
                Parameter[] methodParameters = method.getParameters();
                String params = String.format("%s(%s)", methodName, Arrays.stream(methodParameters).map(item -> item.getType().getSimpleName() + " " + item.getName()).collect(Collectors.joining(", ")));

                job1.setBeanName(StringUtils.uncapitalize(clsSimpleName));
                job1.setMethodName(methodName);
                job1.setParams(params);
                jobList.add(job1);
            }
        }
        return jobList;
    }

}
