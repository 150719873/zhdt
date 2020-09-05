package com.dotop.pipe.server.schedule;

import com.dotop.pipe.core.form.PatrolTaskTimerForm;
import com.dotop.pipe.web.api.factory.patrol.IPatrolTaskTimerFactory;
import com.dotop.pipe.web.api.factory.schedule.IScheduleFactory;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleControll {
    private static final Logger LOGGER = LogManager.getLogger(ScheduleControll.class);

    @Autowired
    private IScheduleFactory iScheduleFactory;
    @Autowired
    private IPatrolTaskTimerFactory iPatrolTaskTimerFactory;

    /**
     * 区域进出口水定时汇总任务 12 小时一次
     */
//    @Scheduled(initialDelay = 5000, fixedRate = 43200000)
    @Scheduled(cron = "0 10 1,13 * * ?")
    public void updateAreaData() {
        LOGGER.info("区域数据汇总执行定时任务-----------");
        try {
            iScheduleFactory.updateData("area");
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
        }
    }


    /**
     * 片区进出口水定时汇总任务 12小时一次
     */
//    @Scheduled(initialDelay = 10000, fixedRate = 43200000)
    @Scheduled(cron = "0 10 1,13 * * ?")
    public void updateRegionData() {
        LOGGER.info("片区数据汇总执行定时任务-----------");
        try {
            iScheduleFactory.updateData("region");
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
        }
    }

    /**
     * 每天0点生成定时巡检任务
     */
    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(initialDelay = 1000, fixedRate = 43200000)
    public void dealPatrolTaskTimer() {
        LOGGER.info("定时分配巡检任务执行定时任务-----------");
        try {
            iPatrolTaskTimerFactory.runTimer(new PatrolTaskTimerForm());
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
        }
        LOGGER.info("定时分配巡检任务执行结束-----------");
    }
}
