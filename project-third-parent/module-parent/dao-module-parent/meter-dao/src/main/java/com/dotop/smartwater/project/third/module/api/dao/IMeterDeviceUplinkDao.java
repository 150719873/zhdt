package com.dotop.smartwater.project.third.module.api.dao;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.dto.DeviceUplinkDto;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceUplinkVo;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *
 */
public interface IMeterDeviceUplinkDao extends BaseDao<DeviceUplinkDto, DeviceUplinkVo> {

    @Override
    void adds(@Param("list") List<DeviceUplinkDto> deviceUplinkDto) throws FrameworkRuntimeException;

    @Override
    List<DeviceUplinkVo> list(DeviceUplinkDto deviceUplinkDto) throws FrameworkRuntimeException;

    @Override
    void edits(@Param("list") List<DeviceUplinkDto> deviceUplinkDto) throws FrameworkRuntimeException;
}
