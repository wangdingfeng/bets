package com.simple.bets;

import com.simple.bets.config.BetsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootApplication(exclude = {MultipartAutoConfiguration.class})
@EnableTransactionManagement
@EnableConfigurationProperties({BetsProperties.class})
@EnableCaching
@EnableAsync
public class BetsApplication {

    private static Logger log = LoggerFactory.getLogger(BetsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BetsApplication.class, args);
        log.info("AdminBETS started up successfully", LocalDate.now(), LocalTime.now());
    }

}

