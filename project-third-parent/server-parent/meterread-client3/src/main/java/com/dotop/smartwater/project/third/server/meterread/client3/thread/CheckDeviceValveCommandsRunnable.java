package com.dotop.smartwater.project.third.server.meterread.client3.thread;

import com.dotop.smartwater.project.third.server.meterread.client3.api.IThirdFactory;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 定时检查设备下发命令
 *
 *
 */
public class CheckDeviceValveCommandsRunnable implements Runnable {

    private static final Logger LOGGER_SYSOUT = LogManager.getLogger("SYSOUT");

    private static final Logger LOGGER = LogManager.getLogger(CheckDeviceValveCommandsRunnable.class);

//    private ThirdFactoryImpl thirdFactoryImpl = (ThirdFactoryImpl) SpringContextUtils.getBean("ThirdFactoryImpl");

    private IThirdFactory iThirdFactory;

    public static boolean temp = false;

    public CheckDeviceValveCommandsRunnable(IThirdFactory iThirdFactory) {
        this.iThirdFactory = iThirdFactory;
    }

    @Override
    public void run() {
        try {
            LOGGER_SYSOUT.info("定时检查设备下发命令任务开始");
            temp = true;
            iThirdFactory.checkDeviceValveCommands();
            temp = false;
            LOGGER_SYSOUT.info("定时检查设备下发命令任务结束");
        } catch (Exception e) {
            temp = false;
            LOGGER_SYSOUT.info("定时检查设备下发命令任务失败");
            LOGGER.error(LogMsg.to(e));
        }
    }
}
