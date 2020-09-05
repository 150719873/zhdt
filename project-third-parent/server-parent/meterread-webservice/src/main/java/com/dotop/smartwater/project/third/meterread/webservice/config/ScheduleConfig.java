package com.dotop.smartwater.project.third.meterread.webservice.config;

import com.dotop.smartwater.project.third.meterread.webservice.schedule.CheckOwnersScheduled;
import com.dotop.smartwater.project.third.meterread.webservice.schedule.RefreshDeviceUplinksScheduled;
import org.springframework.context.annotation.Bean;

//@Configuration
public class ScheduleConfig {


    @Bean
    public CheckOwnersScheduled checkOwnersScheduled() {
        return new CheckOwnersScheduled();
    }

    @Bean
    public RefreshDeviceUplinksScheduled refreshDeviceUplinksScheduled() {
        return new RefreshDeviceUplinksScheduled();
    }
}
