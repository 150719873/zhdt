package com.dotop.smartwater.project.server.pay.config;

import com.dotop.smartwater.project.module.client.third.fegin.pay.PayHystrixFallbackFactory;
import com.dotop.smartwater.project.module.client.third.fegin.pipe.PipeHystrixFallbackFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**

 */
@Configuration
@EnableFeignClients(basePackages = { "com.dotop.smartwater.project.module.client.third.fegin" })
public class BridgeFeignConfig {

	@Bean
	public PipeHystrixFallbackFactory pipeHystrixFallbackFactory() {
		return new PipeHystrixFallbackFactory();
	}
}
