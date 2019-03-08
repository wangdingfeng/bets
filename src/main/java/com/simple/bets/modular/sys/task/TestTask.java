package com.simple.bets.modular.sys.task;

import com.simple.bets.core.annotation.CronTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author wangdingfeng
 * @Description //定时任务测试
 * @Date 15:18 2019/2/2
 **/

@CronTag("testTask")
public class TestTask {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public void test(String params) {
        log.info("我是带参数的test方法，正在被执行，参数为：{}" , params);
    }

    public void test1() {
        log.info("我是不带参数的test1方法，正在被执行");
    }

}
