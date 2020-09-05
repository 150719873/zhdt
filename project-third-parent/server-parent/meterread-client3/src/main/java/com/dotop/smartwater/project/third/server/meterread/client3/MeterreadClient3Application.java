package com.dotop.smartwater.project.third.server.meterread.client3;

import com.dotop.smartwater.project.third.server.meterread.client3.config.TimerConfig;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.annotation.PostConstruct;

@SpringBootApplication(exclude = {RabbitAutoConfiguration.class, RedisAutoConfiguration.class,
        DataSourceAutoConfiguration.class}, scanBasePackages = "com.dotop.smartwater")
public class MeterreadClient3Application {

    private final static Logger LOGGER = LogManager.getLogger(MeterreadClient3Application.class);

    public static void main(String[] args) {
        SpringApplication.run(MeterreadClient3Application.class, args);
    }

    @Autowired
    private TimerConfig timerConfig;

    @PostConstruct
    private void postConstruct() {
        // 先停止，在开启.
        timerConfig.stopCron();
        timerConfig.startCron();
        LOGGER.info(LogMsg.to("msg", "init.startCron()"));
    }
}
