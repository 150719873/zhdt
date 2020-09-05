package com.dotop.smartwater.project.third.server.meterread.client3.thread;

import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.third.server.meterread.client3.api.IThirdFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 定时检查水务系统刷新设备上行读数
 *
 *
 */
public class RefreshDeviceUplinksRunnable implements Runnable {

    private static final Logger LOGGER_SYSOUT = LogManager.getLogger("SYSOUT");

    private static final Logger LOGGER = LogManager.getLogger(RefreshDeviceUplinksRunnable.class);

//    private ThirdFactoryImpl thirdFactoryImpl = (ThirdFactoryImpl) SpringContextUtils.getBean("ThirdFactoryImpl");

    private IThirdFactory iThirdFactory;

    public static boolean temp = false;

    public RefreshDeviceUplinksRunnable(IThirdFactory iThirdFactory) {
        this.iThirdFactory = iThirdFactory;
    }

    @Override
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
