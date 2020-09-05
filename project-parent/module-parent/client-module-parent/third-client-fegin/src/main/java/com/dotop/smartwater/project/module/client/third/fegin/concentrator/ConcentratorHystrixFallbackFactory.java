package com.dotop.smartwater.project.module.client.third.fegin.concentrator;

import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.core.third.vo.concentrator.ConcentratorDeviceVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import feign.hystrix.FallbackFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**

 */
public class ConcentratorHystrixFallbackFactory implements FallbackFactory<IConcentratorFeginClient> {

    private static final Logger LOGGER = LogManager.getLogger(ConcentratorHystrixFallbackFactory.class);

    @Override
    public IConcentratorFeginClient create(Throwable ex) {
        return new IConcentratorFeginClient() {
            @Override
            public void del(DeviceBo device) {
                LOGGER.error(LogMsg.to("ex", ex, "device", device));
            }

            @Override
            public ConcentratorDeviceVo get(DeviceBo device) {
                LOGGER.error(LogMsg.to("ex", ex, "device", device));
                return null;
            }
        };
    }

}
