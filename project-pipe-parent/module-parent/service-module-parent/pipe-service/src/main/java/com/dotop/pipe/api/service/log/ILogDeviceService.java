package com.dotop.pipe.api.service.log;

import com.dotop.pipe.core.bo.log.LogDeviceBo;
import com.dotop.pipe.core.vo.log.LogDeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.List;


public interface ILogDeviceService extends BaseService<LogDeviceBo, LogDeviceVo> {

    @Override
    List<LogDeviceVo> list(LogDeviceBo logDeviceBo) throws FrameworkRuntimeException;

    void adds(List<LogDeviceBo> logDeviceBos) throws FrameworkRuntimeException;

    void dels(LogDeviceBo logDeviceBo) throws FrameworkRuntimeException;
}
