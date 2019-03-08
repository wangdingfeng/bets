package com.simple.bets.core.common.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.simple.bets.core.common.util.IPUtils;
import com.simple.bets.config.BetsProperties;
import com.simple.bets.core.common.util.HttpContextUtils;
import com.simple.bets.modular.sys.model.LogModel;
import com.simple.bets.modular.sys.model.UserModel;
import com.simple.bets.modular.sys.service.LogService;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * AOP 记录用户操作日志
 *
 * @author wangdingfeng
 */
@Aspect
@Component
public class LogAspect {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BetsProperties betsProperties;

    @Autowired
    private LogService logService;


    @Pointcut("@annotation(com.simple.bets.core.annotation.Log)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws JsonProcessingException {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        try {
            // 执行方法
            result = point.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        // 获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        // 设置IP地址
        String ip = IPUtils.getIpAddr(request);
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        if (betsProperties.isOpenAopLog()) {
            // 保存日志
            UserModel user = (UserModel) SecurityUtils.getSubject().getPrincipal();
            LogModel log = new LogModel();
            log.setUsername(user.getUsername());
            log.setIp(ip);
            log.setTime(time);
            logService.saveLog(point, log);
        }
        return result;
    }
}
