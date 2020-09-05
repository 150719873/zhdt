package com.dotop.smartwater.project.third.server.meterread.client2.schedule;

import com.dotop.smartwater.project.third.server.meterread.client2.api.IThirdFactory;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.third.server.meterread.client2.thread.RefreshDeviceUplinksRunnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时检查水务系统刷新设备上行读数
 * http://localhost:35559/meterread-client/third/refreshDeviceUplinks
 *
 *
 */
public class RefreshDeviceUplinksSchedule {

    private static final Logger LOGGER_SYSOUT = LogManager.getLogger("SYSOUT");

    private static final Logger LOGGER = LogManager.getLogger(RefreshDeviceUplinksRunnable.class);

    @Autowired
    private IThirdFactory iThirdFactory;

    public static boolean temp = false;

    //    @Scheduled(initialDelay = 5000, fixedRate = 5 * 60 * 1000)
    @Scheduled(cron = "0 15 10 2-5,28-31 * ?")
//    @Scheduled(cron = "0 15 10 2-5 * ?")
//    @Scheduled(cron = "0 15 1-23 1-5 * ?")
//    @Scheduled(cron = "0 0/2 1-23 * * ?")
    public void run() {
        try {
            LOGGER_SYSOUT.info("定时检查水务系统刷新设备上行读数开始");
            temp = true;
            iThirdFactory.refreshDeviceUplinks();
            temp = false;
            LOGGER_SYSOUT.info("定时检查水务系统刷新设备上行读数结束");
        } catch (Exception e) {
            temp = false;
            LOGGER_SYSOUT.info("定时检查水务系统刷新设备上行读数失败");
            LOGGER.error(LogMsg.to(e));
        }
    }
}
