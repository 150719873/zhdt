package com.dotop.pipe.server.config;

import com.dotop.pipe.server.rest.service.runingdata.RunDataRunnable;
import com.dotop.pipe.api.service.runingdata.IRuningDataService;
import com.dotop.pipe.core.form.RuningDataForm;
import com.dotop.pipe.core.vo.runingdata.RuningDataVo;
import com.dotop.pipe.service.runingdata.RuningDataServiceImpl;
import com.dotop.pipe.web.api.factory.runingdata.IRuningDataFactory;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

/**
 * 定时器配置
 *
 *
 */
@Configuration
public class TimerConfig {

    private static final Logger LOGGER = LogManager.getLogger(TimerConfig.class);

    private static Timer timer = new Timer();

    @Autowired
    private IRuningDataFactory iRuningDataFactory;


    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public static Map<String, ScheduledFuture<?>> futures = new HashMap<>();

    @Bean
    public IRuningDataService runingDataServiceImpl() {
        return new RuningDataServiceImpl();
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(20);
        executor.setThreadNamePrefix("runDataTask-");
        //用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean，这样这些异步任务的销毁就会先于Redis线程池的销毁。
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //该方法用来设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
        executor.setAwaitTerminationSeconds(600);
        return executor;
    }


    /**
     * 创建一个定时任务
     *
     * @param threadName
     * @param startDate
     * @param endDate
     * @param initialDelay 时间间隔 单位分钟
     * @param type         一次跑一个时间范围内的数据  定时任务跑数据
     */
    public void startCron(String threadName, Date startDate, Date endDate, Integer initialDelay, String type, RuningDataForm runingDataForm) {
        if ("once".equals(type)) {
            // 执行一次的定时任务 从开始时间 到结束时间 的时间段内 已一定的间隔 生成数据
            setCronOnce(threadName, new RunDataRunnable(threadName, this.iRuningDataFactory, runingDataForm, this), startDate);
        } else {
            // 执行多次的定时任务  从开始时间起 以一定的时间间隔 执行 直到大于结束时间结束
            setCron(threadName, new RunDataRunnable(threadName, this.iRuningDataFactory, runingDataForm, this), startDate, initialDelay);
        }

        for (String key : futures.keySet()) {
            System.out.println(key);
        }
    }


    /**
     * 创建一个多次执行的定时任务
     *
     * @param key
     * @param task
     * @param startTime
     * @param period
     */
    private void setCron(String key, Runnable task, Date startTime, int period) {
        System.out.println(startTime);
        ScheduledFuture<?> future = threadPoolTaskScheduler.scheduleAtFixedRate(task, startTime, period * 60 * 1000);
        futures.put(key, future);
    }

    /**
     * 创建一个只执行一次的线程
     *
     * @param key
     * @param task
     * @param startTime
     */
    private void setCronOnce(String key, Runnable task, Date startTime) {
        ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(task, startTime);
        futures.put(key, future);
    }

    /**
     * 停止指定keyName的定时任务
     */
    public static void stopCron(String keyName) {
        if (!"".equals(keyName) && keyName != null) {
            if (futures.containsKey(keyName)) {
                ScheduledFuture<?> future = futures.get(keyName);
                if (future != null) {
                    // 停止定时任务
                    boolean flag = future.cancel(true);
                    System.out.println("线程是否取消" + flag);
                    futures.remove(keyName);
                }
            }
        }
    }


    /**
     * 关掉所有的定时任务
     */
    public void stopCronAll() {
        for (String key : futures.keySet()) {
            ScheduledFuture<?> future = futures.get(key);
            if (future != null) {
                future.cancel(true);
            }
        }
        futures = new HashMap<>();
    }

    public void stopCronByKey(String key) {
        ScheduledFuture<?> future = futures.get(key);
        if (future != null) {
            future.cancel(true);
            futures.remove(key);
        }
    }


    // 开启所有未完成的定时任务
    public void startCronAll() {
        stopCronAll();
        List<RuningDataVo> list = iRuningDataFactory.getRuningTask(new RuningDataForm());
        if (list != null && !list.isEmpty()) {
            for (RuningDataVo runingDataVo : list) {
                RuningDataForm runingDataForm = BeanUtils.copyProperties(runingDataVo, RuningDataForm.class);
                //  String threadName, Date startDate, Date endDate, Integer initialDelay, String type, RuningDataForm runingDataForm
                startCron(runingDataForm.getTaskId(), runingDataForm.getNextStartDate(), runingDataForm.getEndDate(), runingDataForm.getInterval(), runingDataForm.getType(), runingDataForm);
            }
        }
        System.out.println(futures.size());
    }


    public void changeCron(RuningDataForm runingDataForm) {
        // 先删掉 再添加数据
        this.stopCronByKey(runingDataForm.getTaskId());
        startCron(runingDataForm.getTaskId(), runingDataForm.getNextStartDate(), runingDataForm.getEndDate(), runingDataForm.getInterval(), runingDataForm.getType(), runingDataForm);
    }

}
