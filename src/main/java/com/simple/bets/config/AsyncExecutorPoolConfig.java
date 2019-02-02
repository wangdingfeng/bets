package com.simple.bets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author wangdingfeng
 * @Description Async线程池 配置  @Async这个注解用于标注某个方法或某个类里面的所有方法都是需要异步处理的。
 * 被注解的方法被调用的时候，会在新线程中执行，而调用它的方法会在原来的线程中执行。这样可以避免阻塞、以及保证任务的实时性。适用于处理log、发送邮件、短信……等
 * @Date 10:18 2019/1/11
 **/
@Configuration
public class AsyncExecutorPoolConfig extends AsyncConfigurerSupport {
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("asyncTaskExecutor-");

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}