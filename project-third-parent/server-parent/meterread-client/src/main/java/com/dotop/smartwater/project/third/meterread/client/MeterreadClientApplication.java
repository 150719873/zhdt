package com.dotop.smartwater.project.third.meterread.client;

import com.dotop.smartwater.project.third.meterread.client.core.utils.SpringContextUtils;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.third.meterread.client.config.TimerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

@SpringBootApplication(exclude = {RabbitAutoConfiguration.class, RedisAutoConfiguration.class,
        DataSourceAutoConfiguration.class}, scanBasePackages = "com.dotop.smartwater")
public class MeterreadClientApplication {

    private final static Logger LOGGER = LogManager.getLogger(MeterreadClientApplication.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(MeterreadClientApplication.class, args);
        SpringContextUtils.setApplicationContext(applicationContext);
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
