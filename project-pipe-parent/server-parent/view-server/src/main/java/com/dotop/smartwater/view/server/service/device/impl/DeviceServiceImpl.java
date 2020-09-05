package com.dotop.smartwater.view.server.service.device.impl;

import com.dotop.smartwater.view.server.service.device.IDeviceService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.view.server.core.device.form.DeviceForm;
import com.dotop.smartwater.view.server.core.device.vo.DeviceVo;
import com.dotop.smartwater.view.server.dao.pipe.device.IViewDeviceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service("DeviceServiceImpl2")
public class DeviceServiceImpl implements IDeviceService {

    @Autowired
    IViewDeviceDao iDeviceDao;

    @Override
    public List<DeviceVo> list(DeviceForm deviceForm) throws FrameworkRuntimeException {
        return iDeviceDao.list(deviceForm);
    }

    @Override
    public Double countLength(DeviceForm deviceForm) throws FrameworkRuntimeException {
        return iDeviceDao.countLength(deviceForm);
    }
}
