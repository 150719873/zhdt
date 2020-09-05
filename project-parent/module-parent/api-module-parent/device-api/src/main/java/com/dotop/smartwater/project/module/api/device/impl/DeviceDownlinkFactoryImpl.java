package com.dotop.smartwater.project.module.api.device.impl;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.api.device.IDeviceDownlinkFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceDownlinkBo;
import com.dotop.smartwater.project.module.core.water.constants.ModeConstants;
import com.dotop.smartwater.project.module.core.water.form.DeviceDownlinkForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;
import com.dotop.smartwater.project.module.service.device.IDeviceDownlinkService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**

 * @date 2019/2/25.
 */
@Component
public class DeviceDownlinkFactoryImpl implements IDeviceDownlinkFactory {

	@Autowired
	private IDeviceDownlinkService iDeviceDownlinkService;

	@Override
	public DeviceDownlinkVo add(DeviceDownlinkForm deviceDownlinkForm){
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		DeviceDownlinkBo deviceDownlinkBo = new DeviceDownlinkBo();
		BeanUtils.copyProperties(deviceDownlinkForm, deviceDownlinkBo);
		deviceDownlinkBo.setEnterpriseid(user.getEnterpriseid());
		deviceDownlinkBo.setUserBy(userBy);
		deviceDownlinkBo.setCurr(curr);
		return iDeviceDownlinkService.add(deviceDownlinkBo);
	}

	@Override
	public List<DeviceDownlinkVo> findByClientId(DeviceDownlinkForm deviceDownlinkForm) {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		DeviceDownlinkBo deviceDownlinkBo = new DeviceDownlinkBo();
		BeanUtils.copyProperties(deviceDownlinkForm, deviceDownlinkBo);
		deviceDownlinkBo.setEnterpriseid(user.getEnterpriseid());
		deviceDownlinkBo.setUserBy(userBy);
		deviceDownlinkBo.setCurr(curr);
		return iDeviceDownlinkService.findByClientId(deviceDownlinkBo);
	}

	@Override
	public void update(DeviceDownlinkForm deviceDownlinkForm) {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		DeviceDownlinkBo deviceDownlinkBo = new DeviceDownlinkBo();
		BeanUtils.copyProperties(deviceDownlinkForm, deviceDownlinkBo);
		deviceDownlinkBo.setEnterpriseid(user.getEnterpriseid());
		deviceDownlinkBo.setUserBy(userBy);
		deviceDownlinkBo.setCurr(curr);
		iDeviceDownlinkService.update(deviceDownlinkBo);
	}

	@Override
	public DeviceDownlinkVo get(DeviceDownlinkForm deviceDownlinkForm) {
		UserVo user = AuthCasClient.getUser();

		DeviceDownlinkBo deviceDownlinkBo = new DeviceDownlinkBo();
		BeanUtils.copyProperties(deviceDownlinkForm, deviceDownlinkBo);

		deviceDownlinkBo.setEnterpriseid(user.getEnterpriseid());

		return iDeviceDownlinkService.get(deviceDownlinkBo);
	}

	@Override
	public Pagination<DeviceDownlinkVo> page(DeviceDownlinkForm deviceDownlinkForm) {
		UserVo user = AuthCasClient.getUser();

		DeviceDownlinkBo deviceDownlinkBo = new DeviceDownlinkBo();
		BeanUtils.copyProperties(deviceDownlinkForm, deviceDownlinkBo);

		deviceDownlinkBo.setEnterpriseid(user.getEnterpriseid());

		Pagination<DeviceDownlinkVo> page = iDeviceDownlinkService.page(deviceDownlinkBo);

		for(DeviceDownlinkVo vo : page.getData()){
			vo.setModeCode(ModeConstants.ModeMap.get(vo.getMode()));
		}

		return page;
	}

	@Override
	public DeviceDownlinkVo getLastDownLink(String devid) {
		return iDeviceDownlinkService.getLastDownLink(devid);
	}
}
