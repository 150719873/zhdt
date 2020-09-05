package com.dotop.pipe.service.devicedata;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.pipe.api.dao.devicedata.IDeviceDataDao;
import com.dotop.pipe.api.service.devicedata.IDeviceDataService;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.form.PointForm;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

@Service
public class DeviceDataServiceImpl implements IDeviceDataService {

	private final static Logger logger = LogManager.getLogger(DeviceDataServiceImpl.class);
	@Autowired
	private IDeviceDataDao iDeviceDataDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void addPointList(List<PointForm> pointList, String operEid, String userBy, Date curr)
			throws FrameworkRuntimeException {
		Integer isNotDel = RootModel.NOT_DEL;
		Integer isDel = RootModel.DEL;
		iDeviceDataDao.addPointList(pointList, operEid, userBy, curr, isNotDel, isDel);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void addDeviceList(List<DeviceForm> deviceList, String operEid, String userBy, Date curr)
			throws FrameworkRuntimeException {
		Integer isNotDel = RootModel.NOT_DEL;
		Integer isDel = RootModel.DEL;
		iDeviceDataDao.addDeviceList(deviceList, operEid, userBy, curr, isNotDel, isDel);
	}

}
