package com.dotop.smartwater.view.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {RabbitAutoConfiguration.class,
        DataSourceAutoConfiguration.class}, scanBasePackages = {"com.dotop.pipe", "com.dotop.smartwater"})
//@MapperScan(basePackages = {"com.dotop.smartwater.view.server.dao", "com.dotop.pipe.api.dao", "com.dotop.pipe.auth.api.dao",
//        "com.dotop.pipe.data.report.api.dao", "com.dotop.pipe.data.receiver.api.dao"})
@EnableScheduling
public class ViewServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViewServerApplication.class, args);
    }

}
