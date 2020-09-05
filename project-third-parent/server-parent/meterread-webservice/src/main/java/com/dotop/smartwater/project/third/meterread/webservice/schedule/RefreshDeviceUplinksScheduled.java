package com.dotop.smartwater.project.third.meterread.webservice.schedule;

import com.dotop.smartwater.project.third.meterread.webservice.core.third.config.Config;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.third.meterread.webservice.api.IWaterFactory;
import com.dotop.smartwater.project.third.meterread.webservice.core.third.config.ConfigEnterprise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * 定时检查水务系统刷新设备上行读数
 *
 *
 */
public class RefreshDeviceUplinksScheduled {

    private static final Logger Logger = LogManager.getLogger(RefreshDeviceUplinksScheduled.class);

    @Autowired
    private IWaterFactory iWaterFactory;

    public static boolean temp = false;

//        @Scheduled(initialDelay = 20000, fixedRate = 43200000)
//    @Scheduled(cron = "0 0 6 1 * ?")
    @Scheduled(cron = "0 25 10 2-5,28-31 * ?")
    public void init() {
        temp = true;
        try {
            Logger.info(LogMsg.to("msg", "定时检查水务系统刷新设备上行读数开始"));
            List<ConfigEnterprise> enterprises = Config.ENTERPRISES;
            for (ConfigEnterprise cs : enterprises) {
                String enterpriseid = cs.getEnterpriseid();
                Integer factoryId = cs.getFactoryId();
                iWaterFactory.refreshDeviceUplinks(enterpriseid, factoryId, null, null, null, null);
            }
        } catch (FrameworkRuntimeException e) {
            temp = false;
            Logger.error(LogMsg.to(e));
        } finally {
            temp = false;
            Logger.info(LogMsg.to("msg", "定时检查水务系统刷新设备上行读数结束"));
        }

    }
}
