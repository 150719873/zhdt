package com.dotop.pipe.server.web;

import com.dotop.pipe.server.config.TimerConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@SpringBootApplication(exclude = {RabbitAutoConfiguration.class,
        DataSourceAutoConfiguration.class}, scanBasePackages = "com.dotop.pipe")
// @EnableCaching
@MapperScan(basePackages = {"com.dotop.pipe.api.dao", "com.dotop.pipe.auth.api.dao",
        "com.dotop.pipe.data.report.api.dao", "com.dotop.pipe.data.receiver.api.dao"})
@EnableEurekaClient
@EnableScheduling
public class PipeServerApplication {

    @Autowired
    private TimerConfig timerConfig;

    public static void main(String[] args) {
        // Map<String, String> map = System.getenv();
        // for (String key : map.keySet()) {
        // System.out.println(key + "==" + map.get(key));
        // }
        for (String arg : args) {
            System.out.println(arg);
            // arg.startsWith("--xjar.password=");
        }
        SpringApplication.run(PipeServerApplication.class, args);
        init();
    }

    private static void init() {
        // GlobalContext.DEBUG = true;
    }

    @PostConstruct
    private void postConstruct() {
        timerConfig.startCronAll();
    }
}
