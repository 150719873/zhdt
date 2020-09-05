package com.dotop.smartwater.project.third.meterread.client.thread;

import com.dotop.smartwater.project.third.meterread.client.api.IThirdFactory;
import com.dotop.smartwater.project.third.meterread.client.config.Config;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 定时检查设备下发命令的状态
 *
 *
 */
public class CheckDeviceValveCommandStatussRunnable implements Runnable {

    private static final Logger LOGGER_SYSOUT = LogManager.getLogger("SYSOUT");

    private static final Logger LOGGER = LogManager.getLogger(CheckDeviceValveCommandStatussRunnable.class);

//    private ThirdFactoryImpl thirdFactoryImpl = (ThirdFactoryImpl) SpringContextUtils.getBean("ThirdFactoryImpl");

    private IThirdFactory iThirdFactory;

    public static boolean temp = false;

    public CheckDeviceValveCommandStatussRunnable(IThirdFactory iThirdFactory) {
        this.iThirdFactory = iThirdFactory;
    }

    @Override
    public void run() {
        try {
            LOGGER_SYSOUT.info("定时检查设备下发命令的状态开始");
            if (iThirdFactory.checkEnterpriseIdExit()) {
                temp = true;
                iThirdFactory.checkDeviceValveCommandStatuss(Config.ENTERPRISE_ID);
                temp = false;
                LOGGER_SYSOUT.info("定时检查设备下发命令的状态结束");
            } else {
                LOGGER_SYSOUT.info("定时检查设备下发命令的状态失败：水务平台中不存在此企业");
            }
        } catch (Exception e) {
            temp = false;
            LOGGER_SYSOUT.info("定时检查设备下发命令的状态失败");
            LOGGER.error(LogMsg.to(e));
        }
    }
}
