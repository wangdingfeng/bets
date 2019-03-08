package com.simple.bets.modular.sys.dao;

import com.simple.bets.core.base.mapper.BaseMapper;
import com.simple.bets.modular.sys.model.JobModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author wangdingfeng
 * @Description //定时任务
 * @Date 15:17 2019/2/2
 **/
@Repository
public interface JobMapper extends BaseMapper<JobModel> {
    /**
     * 查询所有运行的定时任务
     * @return
     */
   List<JobModel> findAllRunJobList();
}