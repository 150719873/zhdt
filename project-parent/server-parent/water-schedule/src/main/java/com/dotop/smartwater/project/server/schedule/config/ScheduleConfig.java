package com.dotop.smartwater.project.server.schedule.config;

import com.dotop.smartwater.project.server.schedule.schedule.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScheduleConfig {

    @Bean
    public CheckoutCouponScheduled checkoutCouponScheduled() {
        return new CheckoutCouponScheduled();
    }

    @Bean
    public DeviceOfflineSchedule deviceOfflineSchedule() {
        return new DeviceOfflineSchedule();
    }

    @Bean
    public EveryDayWaterSchedule everyDayWaterSchedule() {
        //功能存在bug，且不需要定期启动
        return new EveryDayWaterSchedule();
    }

    @Bean
    public DeviceDownLinkSchedule deviceDownLinkSchedule() {
        return new DeviceDownLinkSchedule();
    }

    @Bean
    public OrderTradePenaltySchedule orderTradePenaltySchedule() {
        return new OrderTradePenaltySchedule();
    }

    @Bean
    public WarningNotifySchedule warningNotifySchedule() {
        return new WarningNotifySchedule();
    }

    @Bean
    public MakeOrderScheduled makeOrderScheduled() {
        return new MakeOrderScheduled();
    }

    // @Bean
    // public TestScheduled testScheduled() {
    // return new TestScheduled();
    // }
}
