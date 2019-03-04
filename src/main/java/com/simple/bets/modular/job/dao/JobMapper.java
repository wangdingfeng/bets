package com.simple.bets.modular.job.dao;

import com.simple.bets.core.base.BaseMapper;
import com.simple.bets.modular.job.model.JobModel;

import java.util.List;
/**
 * @Author wangdingfeng
 * @Description //定时任务
 * @Date 15:17 2019/2/2
 **/

public interface JobMapper extends BaseMapper<JobModel> {
	/**
	 * @Author wangdingfeng
	 * @Description //查询所有的定时任务
	 * @Date 15:17 2019/2/2
	 **/
	List<JobModel> queryList();
}