package com.dotop.smartwater.project.third.module.api.service;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceUplinkVo;

import java.util.List;

/**
 *
 */
public interface IMeterDeviceUplinkService extends BaseService<DeviceUplinkBo, DeviceUplinkVo> {

    @Override
    void adds(List<DeviceUplinkBo> deviceUplinkBos) throws FrameworkRuntimeException;

    @Override
    List<DeviceUplinkVo> list(DeviceUplinkBo deviceUplinkBo) throws FrameworkRuntimeException;

    @Override
    void edits(List<DeviceUplinkBo> deviceUplinkBos) throws FrameworkRuntimeException;
}
