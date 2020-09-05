package com.dotop.smartwater.view.server.schedule;

import com.dotop.smartwater.view.server.api.factory.view.*;
import com.dotop.smartwater.view.server.constants.ViewConstants;
import com.dotop.smartwater.view.server.core.device.form.DeviceSummaryForm;
import com.dotop.smartwater.view.server.core.device.form.WaterWmLogForm;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.view.server.api.factory.view.*;
import com.dotop.smartwater.view.server.core.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class ViewSchedule {

    private static final Logger LOGGER = LogManager.getLogger(ViewSchedule.class);

    @Autowired
    IViewFactory iViewFactory;

    @Autowired
    IWaterFactoryViewFactory iWaterFactoryViewFactory;

    @Autowired
    IWaterWmFactory iWaterWmFactory;

    @Autowired
    ISecurityFactory iSecurityFactory;

    @Autowired
    IMonitorFactory iMonitorFactory;

    @Scheduled(initialDelay = 5000, fixedRate = 3600000)
    // @Scheduled(cron = "0 5 * * * ?")
    public void updateTask() {
        LOGGER.info("执行定时任务-----------");
        for (String enterpriseId : Config.ENTERPRISE_IDS.split(",")) {
            try {
                DeviceSummaryForm deviceSummaryForm = new DeviceSummaryForm();
                deviceSummaryForm.setEnterpriseId(enterpriseId);
                String s = iViewFactory.updateTask(deviceSummaryForm);
                LOGGER.info(s);
            } catch (Exception e) {
                LOGGER.error(LogMsg.to(e));
            }
        }
    }

    /**
     * 每天两点十分 执行生成数据
     */
    // @Scheduled(initialDelay = 5000, fixedRate = 36000000)
    @Scheduled(cron = "0 10 2 * * ?")
    public void waterFactoryTaskDay() {
        LOGGER.info("执行定时任务-----------");
        for (String enterpriseId : Config.ENTERPRISE_IDS.split(",")) {
            try {
                DeviceSummaryForm deviceSummaryForm = new DeviceSummaryForm();
                deviceSummaryForm.setEnterpriseId(enterpriseId);
                deviceSummaryForm.setDataDensity(ViewConstants.DATA_DENSITY_DAY);
                String s = iWaterFactoryViewFactory.waterFactoryUpdateTask(deviceSummaryForm);
                LOGGER.info(s);
            } catch (Exception e) {
                LOGGER.error(LogMsg.to(e));
            }
        }
    }

    /**
     * 每小时的十分钟执行一条
     */
    // @Scheduled(initialDelay = 6000, fixedRate = 36000000)
    @Scheduled(cron = "0 10 * * * ?")
    public void waterFactoryTaskHour() {
        LOGGER.info("执行定时任务-----------");
        for (String enterpriseId : Config.ENTERPRISE_IDS.split(",")) {
            try {
                DeviceSummaryForm deviceSummaryForm = new DeviceSummaryForm();
                deviceSummaryForm.setEnterpriseId(enterpriseId);
                deviceSummaryForm.setDataDensity(ViewConstants.DATA_DENSITY_HOUR);
                String s = iWaterFactoryViewFactory.waterFactoryUpdateTask(deviceSummaryForm);
                LOGGER.info(s);
            } catch (Exception e) {
                LOGGER.error(LogMsg.to(e));
            }
        }
    }

    // @Scheduled(initialDelay = 6000, fixedRate = 36000000)
    @Scheduled(cron = "0 5 * * * ?")
    public void waterWmTaskHour() {
        LOGGER.info("执行定时任务-----------");
        for (String enterpriseId : Config.ENTERPRISE_IDS.split(",")) {
            try {
                WaterWmLogForm deviceSummaryForm = new WaterWmLogForm();
                deviceSummaryForm.setEnterpriseId(enterpriseId);
                String s = iWaterWmFactory.updateTask(deviceSummaryForm);
                LOGGER.info(s);
            } catch (Exception e) {
                LOGGER.error(LogMsg.to(e));
            }
        }
    }


    // 门禁刷脸记录
    @Scheduled(cron = "0 5,10,15,20,25,47 9,10,11,14,15,16,18 * * ?")
    // @Scheduled(initialDelay = 6000, fixedRate = 36000000)
    public void securityLog() {
        LOGGER.info("执行定时任务-----------");
        for (String enterpriseId : Config.ENTERPRISE_IDS.split(",")) {
            try {
                String s = iSecurityFactory.updateTask(enterpriseId);
                LOGGER.info(s);
            } catch (Exception e) {
                LOGGER.error(LogMsg.to(e));
            }
        }
    }

    // 液位监控
    @Scheduled(cron = "0 5 * * * ?")
    // @Scheduled(initialDelay = 6000, fixedRate = 36000000)
    public void monitorLiquid() {
        LOGGER.info("执行定时任务-----------");
        for (String enterpriseId : Config.ENTERPRISE_IDS.split(",")) {
            try {
                String s = iMonitorFactory.updateTaskLiquid(enterpriseId);
                LOGGER.info(s);
            } catch (Exception e) {
                LOGGER.error(LogMsg.to(e));
            }
        }
    }

    @Scheduled(cron = "0 5 * * * ?")
    public void monitorAlarm() {
        LOGGER.info("执行定时任务-----------");
        for (String enterpriseId : Config.ENTERPRISE_IDS.split(",")) {
            try {
                String s = iMonitorFactory.updateTaskPondALarm(enterpriseId);
                LOGGER.info(s);
            } catch (Exception e) {
                LOGGER.error(LogMsg.to(e));
            }
        }
    }

}
