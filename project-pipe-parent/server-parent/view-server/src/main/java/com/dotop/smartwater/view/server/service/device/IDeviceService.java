package com.dotop.smartwater.view.server.service.device;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.view.server.core.device.form.DeviceForm;
import com.dotop.smartwater.view.server.core.device.vo.DeviceVo;

import java.util.List;

/**
 *
 */
public interface IDeviceService {

    List<DeviceVo> list(DeviceForm deviceForm) throws FrameworkRuntimeException;

    Double countLength(DeviceForm deviceForm) throws FrameworkRuntimeException;
}
