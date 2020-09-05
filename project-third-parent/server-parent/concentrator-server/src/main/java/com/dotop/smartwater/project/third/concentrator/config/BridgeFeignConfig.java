package com.dotop.smartwater.project.third.concentrator.config;

import com.dotop.smartwater.project.module.client.third.fegin.concentrator.ConcentratorHystrixFallbackFactory;
import com.dotop.smartwater.project.module.client.third.fegin.pay.PayHystrixFallbackFactory;
import com.dotop.smartwater.project.module.client.third.fegin.pipe.PipeHystrixFallbackFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.dotop.smartwater.project.module.client.third.fegin.downlink.ThirdDownHystrixFallbackFactory;

@Configuration
@EnableFeignClients(basePackages = {"com.dotop.smartwater.project.module.client.third.fegin"})
public class BridgeFeignConfig {

    @Bean
    public PipeHystrixFallbackFactory pipeHystrixFallbackFactory() {
        return new PipeHystrixFallbackFactory();
    }

    @Bean
    public PayHystrixFallbackFactory payHystrixFallbackFactory() {
        return new PayHystrixFallbackFactory();
    }

    @Bean
    public ConcentratorHystrixFallbackFactory concentratorHystrixFallbackFactory() {
        return new ConcentratorHystrixFallbackFactory();
    }
    @Bean
    public ThirdDownHystrixFallbackFactory thirdDownHystrixFallbackFactory() {
        return new ThirdDownHystrixFallbackFactory();
    }

}
