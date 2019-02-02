package com.simple.bets.config;

import com.simple.bets.core.common.interceptor.SqlStatementInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author wangdingfeng
 * @Description mybatis 配置项
 * @Date 10:25 2019/1/11
 **/

@Configuration
@MapperScan("com.simple.bets.modular.*.dao")
public class MyBatisConfig {

    /**
     * 配置 sql打印拦截器
     * application.yml中 febs.showsql: true 时生效
     *
     * @return SqlStatementInterceptor
     */
    @Bean
    @ConditionalOnProperty(name = "bets.showsql", havingValue = "true")
    SqlStatementInterceptor sqlStatementInterceptor() {
        return new SqlStatementInterceptor();
    }
}
