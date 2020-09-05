package com.dotop.smartwater.project.third.server.meterread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {RabbitAutoConfiguration.class,
        DataSourceAutoConfiguration.class}, scanBasePackages = "com.dotop.smartwater")
//@EnableScheduling
//@EnableAsync
public class MeterreadServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeterreadServerApplication.class, args);
    }

}
