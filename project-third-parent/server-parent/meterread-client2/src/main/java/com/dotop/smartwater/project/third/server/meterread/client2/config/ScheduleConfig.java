package com.dotop.smartwater.project.third.server.meterread.client2.config;

import com.dotop.smartwater.project.third.server.meterread.client2.schedule.RefreshDeviceUplinksSchedule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class ScheduleConfig {

    @Bean
    public RefreshDeviceUplinksSchedule refreshDeviceUplinksSchedule() {
        return new RefreshDeviceUplinksSchedule();
    }


}
