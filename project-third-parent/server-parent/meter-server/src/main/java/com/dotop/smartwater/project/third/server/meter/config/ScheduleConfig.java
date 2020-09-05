package com.dotop.smartwater.project.third.server.meter.config;

import com.dotop.smartwater.project.third.server.meter.schedule.CommandSchedule;
import com.dotop.smartwater.project.third.server.meter.schedule.MeterSchedule;
import org.springframework.context.annotation.Bean;

/**
 *
 */
//@Configuration
public class ScheduleConfig {

    @Bean
    public MeterSchedule meterSchedule() {
        return new MeterSchedule();
    }

    @Bean
    public CommandSchedule commandSchedule() {
        return new CommandSchedule();
    }
}
