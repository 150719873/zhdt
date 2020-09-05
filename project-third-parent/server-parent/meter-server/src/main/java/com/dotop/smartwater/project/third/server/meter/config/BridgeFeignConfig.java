package com.dotop.smartwater.project.third.server.meter.config;

import com.dotop.smartwater.project.third.module.client.fegin.CasHystrixFallbackFactory;
import com.dotop.smartwater.project.third.module.client.fegin.WaterHystrixFallbackFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"com.dotop.smartwater.project.third.module.client.fegin"})
public class BridgeFeignConfig {


    @Bean
    public WaterHystrixFallbackFactory waterHystrixFallbackFactory() {
        return new WaterHystrixFallbackFactory();
    }

    @Bean
    public CasHystrixFallbackFactory casHystrixFallbackFactory() {
        return new CasHystrixFallbackFactory();
    }
}
