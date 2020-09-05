package com.dotop.smartwater.project.third.server.meterread.client3.config;

import com.dotop.smartwater.project.third.server.meterread.client3.api.IThirdFactory;
import com.dotop.smartwater.project.third.server.meterread.client3.thread.CheckOwnersRunnable;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.third.server.meterread.client3.thread.CheckDeviceValveCommandStatussRunnable;
import com.dotop.smartwater.project.third.server.meterread.client3.thread.CheckDeviceValveCommandsRunnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * 定时器配置
 *
 *
 */
@Configuration
public class TimerConfig {

    private static final Logger LOGGER = LogManager.getLogger(TimerConfig.class);

    @Value("${param.config.timer.Test:10}")
    private int TEST;

    @Value("${param.config.timer.CheckDeviceValveCommands:30}")
    private int CHECK_DEVICE_VALVE_COMMANDS;

    @Value("${param.config.timer.CheckDeviceValveCommandStatuss:30}")
    private int CHECK_DEVICE_VALVE_COMMAND_STATUSS;

    @Value("${param.config.timer.CheckOwners:60}")
    private int CHECK_OWNERS;

    @Value("${param.config.timer.RefreshDeviceUplinks:30}")
    private int REFRESH_DEVICE_UPLINKS;


    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(20);
        executor.setThreadNamePrefix("taskExecutor-");
        //用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean，这样这些异步任务的销毁就会先于Redis线程池的销毁。
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //该方法用来设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
        executor.setAwaitTerminationSeconds(600);
        return executor;
    }

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private Map<String, ScheduledFuture<?>> futures = new HashMap<>();

    @Autowired
    private IThirdFactory iThirdFactory;

    public void startCron() {
        Date curr = new Date();
        setCron("CheckDeviceValveCommandsRunnable", new CheckDeviceValveCommandsRunnable(iThirdFactory), curr, 20, CHECK_DEVICE_VALVE_COMMANDS);
        setCron("CheckDeviceValveCommandStatussRunnable", new CheckDeviceValveCommandStatussRunnable(iThirdFactory), curr, 30, CHECK_DEVICE_VALVE_COMMAND_STATUSS);
        // TODO 以业主更新为主
//        setCron("RefreshDeviceUplinksRunnable", new RefreshDeviceUplinksRunnable(iThirdFactory), curr, 10, REFRESH_DEVICE_UPLINKS);
        setCron("CheckOwnersRunnable", new CheckOwnersRunnable(iThirdFactory), curr, 10, CHECK_OWNERS);
    }

    private void setCron(String key, Runnable task, Date curr, int initialDelay, int period) {
        Date startTime = DateUtils.second(curr, initialDelay);
        ScheduledFuture<?> future = threadPoolTaskScheduler.scheduleAtFixedRate(task, startTime, period * 1000);
        futures.put(key, future);
    }

    public void stopCron() {
        for (String key : futures.keySet()) {
            ScheduledFuture<?> future = futures.get(key);
            if (future != null) {
                future.cancel(true);
            }
        }
    }
}
