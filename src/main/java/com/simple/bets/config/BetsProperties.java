package com.simple.bets.config;

import com.simple.bets.core.shiro.ShiroProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author wangdingfeng
 * @Description bets自定义配置
 * @Date 10:24 2019/1/11
 **/

@Configuration
@ConfigurationProperties(prefix = "bets")
public class BetsProperties {
    /**
     * shiro配置项
     */
    private ShiroProperties shiro = new ShiroProperties();

    private String timeFormat = "yyyy-MM-dd HH:mm:ss";

    private boolean openAopLog = true;
    /**
     * 文件保存路径
     */
    private String filePath;

    public ShiroProperties getShiro() {
        return shiro;
    }

    public void setShiro(ShiroProperties shiro) {
        this.shiro = shiro;
    }


    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public boolean isOpenAopLog() {
        return openAopLog;
    }

    public void setOpenAopLog(boolean openAopLog) {
        this.openAopLog = openAopLog;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
