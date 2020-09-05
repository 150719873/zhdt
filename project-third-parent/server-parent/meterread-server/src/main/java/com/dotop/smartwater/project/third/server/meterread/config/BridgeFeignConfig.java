package com.dotop.smartwater.project.third.server.meterread.config;

import com.dotop.smartwater.project.third.server.meterread.client.fegin.CasHystrixFallbackFactory;
import com.dotop.smartwater.project.third.server.meterread.client.fegin.WaterDownLinkFallbackFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"com.dotop.smartwater.project.third.server.meterread.client.fegin"})
public class BridgeFeignConfig {

    @Bean
    public WaterDownLinkFallbackFactory waterDownLinkFallbackFactory() {
        return new WaterDownLinkFallbackFactory();
    }

    @Bean
    public CasHystrixFallbackFactory casHystrixFallbackFactory() {
        return new CasHystrixFallbackFactory();
    }
}
