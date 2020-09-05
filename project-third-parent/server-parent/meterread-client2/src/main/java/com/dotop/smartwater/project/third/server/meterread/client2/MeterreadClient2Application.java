package com.dotop.smartwater.project.third.server.meterread.client2;

import com.dotop.smartwater.project.third.server.meterread.client2.utils.SpringContextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {RabbitAutoConfiguration.class, RedisAutoConfiguration.class,
        DataSourceAutoConfiguration.class}, scanBasePackages = "com.dotop.smartwater")
@EnableScheduling
public class MeterreadClient2Application {

    private final static Logger LOGGER = LogManager.getLogger(MeterreadClient2Application.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(MeterreadClient2Application.class, args);
        SpringContextUtils.setApplicationContext(applicationContext);
    }

}
