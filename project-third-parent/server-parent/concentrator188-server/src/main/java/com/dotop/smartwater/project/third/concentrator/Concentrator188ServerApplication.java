package com.dotop.smartwater.project.third.concentrator;

import com.dotop.smartwater.project.third.concentrator.core.utils.SpringContextUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(exclude = {RabbitAutoConfiguration.class,
        DataSourceAutoConfiguration.class}, scanBasePackages = "com.dotop.smartwater")
@MapperScan(basePackages = {"com.dotop.smartwater.project.module.dao", "com.dotop.smartwater.project.third.concentrator.dao"})
public class Concentrator188ServerApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Concentrator188ServerApplication.class, args);
        SpringContextUtils.setApplicationContext(applicationContext);
    }

}
