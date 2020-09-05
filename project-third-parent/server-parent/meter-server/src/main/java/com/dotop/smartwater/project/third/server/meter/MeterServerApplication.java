package com.dotop.smartwater.project.third.server.meter;

import com.dotop.smartwater.project.module.core.third.utils.oss.OssUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(exclude = {RabbitAutoConfiguration.class,
        DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = "com.dotop.smartwater", excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {OssUtil.class}))
@EnableScheduling
public class MeterServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeterServerApplication.class, args);
    }

}
