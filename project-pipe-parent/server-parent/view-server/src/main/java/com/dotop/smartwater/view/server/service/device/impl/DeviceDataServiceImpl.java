package com.dotop.smartwater.view.server.service.device.impl;

import com.dotop.smartwater.view.server.service.device.IDeviceDataService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.view.server.core.device.form.DeviceDataForm;
import com.dotop.smartwater.view.server.dao.pipe.device.IViewDeviceDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service("DeviceDataServiceImpl2")
public class DeviceDataServiceImpl implements IDeviceDataService {

    @Autowired
    IViewDeviceDataDao iDeviceDataDao;

    @Override
    public List list(DeviceDataForm deviceDataForm) throws FrameworkRuntimeException {
        return iDeviceDataDao.list(deviceDataForm);
    }
}
