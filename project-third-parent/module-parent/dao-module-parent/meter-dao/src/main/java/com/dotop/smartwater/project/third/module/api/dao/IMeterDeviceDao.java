package com.dotop.smartwater.project.third.module.api.dao;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.dto.DeviceDto;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *
 */
public interface IMeterDeviceDao extends BaseDao<DeviceDto, DeviceVo> {

    @Override
    List<DeviceVo> list(DeviceDto deviceDto) throws FrameworkRuntimeException;

    @Override
    void adds(@Param("list") List<DeviceDto> deviceDtos) throws FrameworkRuntimeException;

    @Override
    void edits(@Param("list") List<DeviceDto> deviceDtos) throws FrameworkRuntimeException;

    @Override
    DeviceVo get(DeviceDto deviceDto) throws FrameworkRuntimeException;
}
