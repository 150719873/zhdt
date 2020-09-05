package com.dotop.smartwater.view.server.service.device;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.view.server.core.device.form.DeviceDataForm;
import com.dotop.smartwater.view.server.core.device.vo.DeviceDataVo;

import java.util.List;

/**
 *
 */
public interface IDeviceDataService {

    List<DeviceDataVo> list(DeviceDataForm deviceDataForm) throws FrameworkRuntimeException;
}
