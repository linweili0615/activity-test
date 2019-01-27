package com.test.jm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableEurekaClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})  //禁用数据源自动配置
@EnableScheduling
public class JmApplication {

    public static void main(String[] args) {
        SpringApplication.run(JmApplication.class, args);
    }
}
