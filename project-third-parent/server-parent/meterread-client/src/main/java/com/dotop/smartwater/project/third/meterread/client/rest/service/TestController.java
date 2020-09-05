package com.dotop.smartwater.project.third.meterread.client.rest.service;

import com.dotop.smartwater.project.third.meterread.client.api.IThirdFactory;
import com.dotop.smartwater.project.third.meterread.client.thread.*;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.third.meterread.client.thread.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Deprecated
//@RestController()
//@RequestMapping("/test")
public class TestController implements BaseController<BaseForm> {

    private final static Logger LOGGER = LogManager.getLogger(TestController.class);

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    private Map<String, ScheduledFuture<?>> futures = new HashMap<>();

    @Autowired
    private IThirdFactory iThirdFactory;

    //    @PostConstruct
    private void postConstruct() {
        stopCron();// 先停止，在开启.
//        command();
        System.out.println("init.startCron()");
    }

    private void command() {
        Date startTime = DateUtils.second(new Date(), 10);

        ScheduledFuture<?> future = threadPoolTaskScheduler.scheduleAtFixedRate(new TestRunnable(), startTime, 5000);
        futures.put("TestRunnable", future);

        future = threadPoolTaskScheduler.scheduleAtFixedRate(new CheckDeviceValveCommandsRunnable(iThirdFactory), startTime, 6000);
        futures.put("CheckDeviceValveCommandsRunnable", future);

        future = threadPoolTaskScheduler.scheduleAtFixedRate(new CheckDeviceValveCommandStatussRunnable(iThirdFactory), startTime, 7000);
        futures.put("CheckDeviceValveCommandStatussRunnable", future);

        future = threadPoolTaskScheduler.scheduleAtFixedRate(new CheckOwnersRunnable(iThirdFactory), startTime, 8000);
        futures.put("CheckOwnersRunnable", future);

        future = threadPoolTaskScheduler.scheduleAtFixedRate(new RefreshDeviceUplinksRunnable(iThirdFactory), startTime, 9000);
        futures.put("RefreshDeviceUplinksRunnable", future);


    }


    /**
     * 启动任务
     **/
    @RequestMapping("/startTask")
    public String startCron() {
        stopCron();// 先停止，在开启.
        command();
        System.out.println("DynamicTaskController.startCron()");
        return "startTask";
    }

    /**
     * 停止任务
     **/
    @RequestMapping("/stopTask")
    public String stopCron() {
        for (String key : futures.keySet()) {
            ScheduledFuture<?> future = futures.get(key);
            if (future != null) {
                future.cancel(true);
            }
        }
        System.out.println("DynamicTaskController.stopCron()");
        return "stopTask";
    }

    /**
     * 变更任务间隔，再次启动
     **/
    @RequestMapping("/changeCron")
    public String changeCron() {
        stopCron();// 先停止，在开启.
        command();
        System.out.println("DynamicTaskController.changeCron()");
        return "changeCron";
    }


}
