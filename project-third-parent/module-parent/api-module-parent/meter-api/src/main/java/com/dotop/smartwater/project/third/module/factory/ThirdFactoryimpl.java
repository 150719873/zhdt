package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;
import com.dotop.smartwater.project.third.module.api.factory.IThirdFactory;
import com.dotop.smartwater.project.third.module.api.service.IWaterDeviceDownlinkService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    public List<DeviceDownlinkVo> getDownlink(List<String> clientIds) throws FrameworkRuntimeException {
        List<DeviceDownlinkVo> list = iWaterDeviceDownlinkService.list(clientIds);
        List<DeviceDownlinkVo> deviceDownlinkVos = new ArrayList<>();
        list.forEach(v -> {
            DeviceDownlinkVo deviceDownlinkVo = new DeviceDownlinkVo();
            deviceDownlinkVo.setClientid(v.getClientid());
            deviceDownlinkVo.setDevno(v.getDevno());
            deviceDownlinkVo.setDeveui(v.getDeveui());
            deviceDownlinkVo.setStatus(v.getStatus());
            deviceDownlinkVos.add(deviceDownlinkVo);
        });
        return deviceDownlinkVos;
    }
}
