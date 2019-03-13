package com.simple.bets.modular.sys.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.simple.bets.modular.sys.model.LogModel;
import com.simple.bets.core.base.service.IService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * 日志service
 * @author wangdingfeng
 * @Date 2019-01-07
 */
public interface LogService extends IService<LogModel> {

	/**
	 * 删除日志
	 * @param logIds
	 */
	void deleteLogs(String logIds);

	/**
	 * 保存日志
	 * @param point
	 * @param log
	 * @throws JsonProcessingException
	 */
	@Async
	void saveLog(ProceedingJoinPoint point, LogModel log) throws JsonProcessingException;
}
