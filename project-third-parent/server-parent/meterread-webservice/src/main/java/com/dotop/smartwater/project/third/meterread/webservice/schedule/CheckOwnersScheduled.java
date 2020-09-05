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
 * 定时检查水务系统检查业主信息
 *
 *
 */
public class CheckOwnersScheduled {

    private static final Logger Logger = LogManager.getLogger(CheckOwnersScheduled.class);

    @Autowired
    private IWaterFactory iWaterFactory;

    public static boolean temp = false;

    //        @Scheduled(initialDelay = 10000, fixedRate = 1800000)
//    @Scheduled(cron = "0 0 5 1 * ?")
    @Scheduled(cron = "0 15 10 2-5,28-31 * ?")
    public void init() {
        temp = true;
        try {
            Logger.info(LogMsg.to("msg", "定时检查水务系统检查业主信息开始"));
            List<ConfigEnterprise> enterprises = Config.ENTERPRISES;
            Logger.info(LogMsg.to("enterprises", enterprises));
            for (ConfigEnterprise cs : enterprises) {
                String enterpriseid = cs.getEnterpriseid();
                Integer factoryId = cs.getFactoryId();
                iWaterFactory.checkOwners(enterpriseid, factoryId, null, null, null);
            }
        } catch (FrameworkRuntimeException e) {
            temp = false;
            Logger.error(LogMsg.to(e));
        } finally {
            temp = false;
            Logger.info(LogMsg.to("msg", "定时检查水务系统检查业主信息结束"));
        }

    }
}
