package com.simple.bets.config;

import com.simple.bets.core.beetl.BeetlConfiguration;
import org.beetl.core.resource.WebAppResourceLoader;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

import java.io.IOException;

/**
 * @ClassName: BeetlConfig
 * @Author: wangdingfeng
 * @Description: beetl配置项 详细配置请参看beetl官网
 * @Date: 2018/11/29 18:12
 * @Version: 1.0
 * */

@Configuration
public class BeetlConf {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * beetl配置
     * @return
     */
    @Bean(initMethod = "init", name = "beetlConfig")
    public BeetlConfiguration getBeetlGroupUtilConfiguration() {
        BeetlConfiguration beetlGroupUtilConfiguration = new BeetlConfiguration();
        ResourcePatternResolver patternResolver = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());
        try {
            // WebAppResourceLoader 配置root路径是关键
            WebAppResourceLoader webAppResourceLoader =
                    new WebAppResourceLoader(patternResolver.getResource("classpath:/").getFile().getPath());
            beetlGroupUtilConfiguration.setResourceLoader(webAppResourceLoader);
        } catch (IOException e) {
            logger.error("beetl配置不存在，请查看classpath下是否有此配置",e);
        }
        logger.info("加载beetl.properties配置成功");
        //读取配置文件信息
        return beetlGroupUtilConfiguration;
    }

    /**
     * 视图层配置
     * @param beetlGroupUtilConfiguration
     * @return
     */
    @Bean(name = "beetlViewResolver")
    public BeetlSpringViewResolver getBeetlSpringViewResolver(
            @Qualifier("beetlConfig") BeetlConfiguration beetlGroupUtilConfiguration) {
        BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
        beetlSpringViewResolver.setSuffix(".html");
        beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
        beetlSpringViewResolver.setOrder(0);
        beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
        return beetlSpringViewResolver;
    }
}
