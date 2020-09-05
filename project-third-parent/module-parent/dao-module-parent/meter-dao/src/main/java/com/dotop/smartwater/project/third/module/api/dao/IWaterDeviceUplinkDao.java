package com.dotop.smartwater.project.third.module.api.dao;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.dto.DeviceUplinkDto;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceUplinkVo;

import java.util.List;

/**
 *
 */
public interface IWaterDeviceUplinkDao extends BaseDao<DeviceUplinkDto, DeviceUplinkVo> {

    @Override
    List<DeviceUplinkVo> list(DeviceUplinkDto deviceUplinkDto) throws FrameworkRuntimeException;

    List<DeviceUplinkVo> listDegrees(DeviceUplinkDto deviceUplinkDto) throws FrameworkRuntimeException;
}
