package com.dotop.smartwater.view.server.dao.pipe.device;

import com.dotop.smartwater.view.server.core.device.form.DeviceForm;
import com.dotop.smartwater.view.server.core.device.vo.DeviceVo;

import java.util.List;

/**
 *
 */
public interface IViewDeviceDao {

    List<DeviceVo> list(DeviceForm deviceForm);

    Double countLength(DeviceForm deviceForm);
}
