package com.dotop.smartwater.view.server.dao.pipe.device;

import com.dotop.smartwater.view.server.core.device.form.DeviceDataForm;
import com.dotop.smartwater.view.server.core.device.vo.DeviceDataVo;

import java.util.List;

/**
 *
 */
public interface IViewDeviceDataDao {

    List<DeviceDataVo> list(DeviceDataForm deviceDataForm);
}
