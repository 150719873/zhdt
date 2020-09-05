package com.dotop.smartwater.project.third.module.api.service;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceUplinkVo;

import java.util.List;

/**
 *
 */
public interface IWaterDeviceUplinkService extends BaseService<DeviceUplinkBo, DeviceUplinkVo> {

    @Override
    List<DeviceUplinkVo> list(DeviceUplinkBo deviceUplinkBo) throws FrameworkRuntimeException;

    List<DeviceUplinkVo> listDegrees(DeviceUplinkBo deviceUplinkBo) throws FrameworkRuntimeException;
}
