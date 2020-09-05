package com.dotop.smartwater.project.third.module.api.service;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;

import java.util.List;

/**
 *
 */
public interface IMeterDeviceService extends BaseService<DeviceBo, DeviceVo> {

    @Override
    List<DeviceVo> list(DeviceBo deviceBo) throws FrameworkRuntimeException;

    @Override
    void adds(List<DeviceBo> deviceBos) throws FrameworkRuntimeException;

    @Override
    void edits(List<DeviceBo> deviceBos) throws FrameworkRuntimeException;

    @Override
    DeviceVo get(DeviceBo deviceBo) throws FrameworkRuntimeException;
}
