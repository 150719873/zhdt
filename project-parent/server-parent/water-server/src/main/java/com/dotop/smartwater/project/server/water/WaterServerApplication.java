package com.dotop.smartwater.project.server.water;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { RabbitAutoConfiguration.class,
		DataSourceAutoConfiguration.class }, scanBasePackages = "com.dotop.smartwater")
@MapperScan(basePackages = { "com.dotop.smartwater.project.module.dao" })
public class WaterServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaterServerApplication.class, args);
	}

}
