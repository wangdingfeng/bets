package com.simple.bets.modular.sys.service.impl;

import com.simple.bets.core.base.service.impl.ServiceImpl;
import com.simple.bets.modular.sys.model.JobLogModel;
import com.simple.bets.modular.sys.service.JobLogService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("jobLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JobLogServiceImpl extends ServiceImpl<JobLogModel> implements JobLogService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public List<JobLogModel> findAllJobLogs(JobLogModel jobLog) {
		try {
			Example example = new Example(JobLogModel.class);
			Criteria criteria = example.createCriteria();
			if (StringUtils.isNotBlank(jobLog.getBeanName())) {
				criteria.andCondition("bean_name=", jobLog.getBeanName());
			}
			if (StringUtils.isNotBlank(jobLog.getMethodName())) {
				criteria.andCondition("method_name=", jobLog.getMethodName());
			}
			if (StringUtils.isNotBlank(jobLog.getStatus())) {
				criteria.andCondition("status=", Long.valueOf(jobLog.getStatus()));
			}
			example.setOrderByClause("log_id desc");
			return this.selectByExample(example);
		} catch (Exception e) {
			log.error("获取调度日志信息失败", e);
			return new ArrayList<>();
		}
	}

	@Override
	@Transactional
	public void saveJobLog(JobLogModel log) {
		this.save(log);
	}

	@Override
	@Transactional
	public void deleteBatch(String jobLogIds) {
		List<String> list = Arrays.asList(jobLogIds.split(","));
		this.batchDelete(list, "logId", JobLogModel.class);
	}

}
