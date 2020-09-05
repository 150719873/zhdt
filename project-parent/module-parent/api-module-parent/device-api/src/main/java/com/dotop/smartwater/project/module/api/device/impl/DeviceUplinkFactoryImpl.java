package com.dotop.smartwater.project.module.api.device.impl;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.api.device.IDeviceUplinkFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.module.service.device.IDeviceUplinkService;

/**

 * @date 2019/2/25.
 */
@Component
public class DeviceUplinkFactoryImpl implements IDeviceUplinkFactory {

	private final static Logger logger = LogManager.getLogger(DeviceUplinkFactoryImpl.class);

	@Autowired
	private IDeviceUplinkService iDeviceUplinkService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DeviceUplinkVo add(DeviceUplinkForm deviceUplinkForm) {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		DeviceUplinkBo deviceUplinkBo = new DeviceUplinkBo();
		BeanUtils.copyProperties(deviceUplinkForm, deviceUplinkBo);
		deviceUplinkBo.setEnterpriseid(user.getEnterpriseid());
		deviceUplinkBo.setUserBy(userBy);
		deviceUplinkBo.setCurr(curr);
		return iDeviceUplinkService.add(deviceUplinkBo);
	}

	@Override
	public DeviceUplinkVo findLastUplinkWater(Long id, String date) throws FrameworkRuntimeException {
		return null;
	}

	@Override
	public Pagination<DeviceUplinkVo> findOriginal(DeviceUplinkForm deviceUplinkForm) throws FrameworkRuntimeException {
		return null;
	}

	@Override
	public Pagination<DeviceUplinkVo> findOriginalCrossMonth(DeviceUplinkForm deviceUplinkForm)
			throws FrameworkRuntimeException {
		return null;
	}

	@Override
	public Pagination<DeviceUplinkVo> findDownLink(DeviceForm deviceForm, String start, String end)
			throws FrameworkRuntimeException {
		return null;
	}
}
