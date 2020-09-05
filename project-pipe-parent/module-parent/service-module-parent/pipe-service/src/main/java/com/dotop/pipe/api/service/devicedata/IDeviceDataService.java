package com.dotop.pipe.api.service.devicedata;

import java.util.Date;
import java.util.List;

import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.form.PointForm;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

public interface IDeviceDataService {

	void addPointList(List<PointForm> pointList, String operEid, String userBy, Date curr)
			throws FrameworkRuntimeException;

	void addDeviceList(List<DeviceForm> deviceList, String operEid, String userBy, Date curr)
			throws FrameworkRuntimeException;
}
