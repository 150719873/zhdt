package com.dotop.smartwater.view.server.config;

import com.dotop.pipe.api.client.fegin.water.WaterHystrixFallbackFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"com.dotop.pipe.api.client.fegin"})
public class BridgeFeignConfig {

    @Bean
    public WaterHystrixFallbackFactory waterHystrixFallbackFactory() {
        return new WaterHystrixFallbackFactory();
    }
}
