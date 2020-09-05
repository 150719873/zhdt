package com.dotop.smartwater.project.third.server.meterread.api.impl;

import com.dotop.smartwater.project.third.server.meterread.api.IThirdFactory;
import com.dotop.smartwater.project.third.server.meterread.service.water.IWaterDeviceDownlinkService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.water.bo.DeviceDownlinkBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 下行命令定时任务
 *
 */
@Component
public class ThirdFactoryimpl implements IThirdFactory {

    private static final Logger LOGGER = LogManager.getLogger(ThirdFactoryimpl.class);


    @Autowired
    IWaterDeviceDownlinkService iWaterDeviceDownlinkService;

    @Override
    public List<DeviceDownlinkVo> getDownlink(List<DeviceDownlinkBo> deviceDownlinkBos) throws FrameworkRuntimeException {
        return iWaterDeviceDownlinkService.list(deviceDownlinkBos);
    }
}
