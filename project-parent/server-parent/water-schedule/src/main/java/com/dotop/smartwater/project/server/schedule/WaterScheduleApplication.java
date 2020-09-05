package com.dotop.smartwater.project.server.schedule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = { RabbitAutoConfiguration.class,
		DataSourceAutoConfiguration.class }, scanBasePackages = "com.dotop.smartwater")
@MapperScan(basePackages = { "com.dotop.smartwater.project.module.dao" })
@EnableScheduling
public class WaterScheduleApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaterScheduleApplication.class, args);
	}

}
