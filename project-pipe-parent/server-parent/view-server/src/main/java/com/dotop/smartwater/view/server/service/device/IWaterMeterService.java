package com.dotop.smartwater.view.server.service.device;


import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.List;

/**
 *
 */
public interface IWaterMeterService {


    DevicePropertyVo waterMeterData(DeviceBo deviceBo)throws FrameworkRuntimeException;

    List<String> brustPipeUnHandler(DeviceBo deviceBo)throws FrameworkRuntimeException;
}
