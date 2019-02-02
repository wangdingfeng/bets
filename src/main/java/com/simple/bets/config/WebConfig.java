package com.simple.bets.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.bets.core.common.xss.XssFilter;
import com.simple.bets.core.controller.BetsErrorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author wangdingfeng
 * @Description WebConfig
 * @Date 10:29 2019/1/11
 **/
@Configuration
public class WebConfig implements  WebMvcConfigurer {

    @Autowired
    private BetsProperties betsProperties;

    /**
     * @Author wangdingfeng
     * @Description 配置虚拟路径 以提供img直接读取
     * @Date 13:37 2019/1/16
     **/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/file/**").addResourceLocations("file:"+betsProperties.getFilePath());
    }

    /**
     * XssFilter Bean
     */
    @Bean
    public FilterRegistrationBean xssFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("excludes", "/favicon.ico,/img/*,/js/*,/css/*,/fonts/*,/file/*");
        initParameters.put("isIncludeRichText", "true");
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }

    @Bean
    public ObjectMapper getObjectMapper(BetsProperties betsProperties) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat(betsProperties.getTimeFormat()));
        return mapper;
    }

    /**
     * 默认错误页面，返回json
     */
    @Bean("error")
    public BetsErrorView error() {
        return new BetsErrorView();
    }

}