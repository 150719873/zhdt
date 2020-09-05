package com.dotop.smartwater.project.module.api.device.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceWarningSettingFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceWarningSettingBo;
import com.dotop.smartwater.project.module.core.water.form.DeviceWarningSettingForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningSettingVo;
import com.dotop.smartwater.project.module.service.device.IDeviceWarningSettingService;

/**

 * @date 2019/4/1.
 */
@Component
public class DeviceWarningSettingFactoryImpl implements IDeviceWarningSettingFactory {


	@Autowired
	private IDeviceWarningSettingService iDeviceWarningSettingService;

	@Override
	public Pagination<DeviceWarningSettingVo> page(DeviceWarningSettingForm deviceWarningSettingForm) {
		UserVo user = AuthCasClient.getUser();

		DeviceWarningSettingBo deviceWarningSettingBo = new DeviceWarningSettingBo();
		BeanUtils.copyProperties(deviceWarningSettingForm, deviceWarningSettingBo);

		deviceWarningSettingBo.setEnterpriseid(user.getEnterpriseid());

		return iDeviceWarningSettingService.page(deviceWarningSettingBo);
	}

	@Override
	public DeviceWarningSettingVo edit(DeviceWarningSettingForm deviceWarningSettingForm) {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();

		DeviceWarningSettingBo deviceWarningSettingBo = new DeviceWarningSettingBo();
		BeanUtils.copyProperties(deviceWarningSettingForm, deviceWarningSettingBo);

		if (StringUtils.isEmpty(deviceWarningSettingForm.getId())) {
			deviceWarningSettingBo.setId(UuidUtils.getUuid());
			deviceWarningSettingBo.setCreateBy(userBy);
			deviceWarningSettingBo.setCreateDate(curr);
		}

		deviceWarningSettingBo.setEnterpriseid(user.getEnterpriseid());

		deviceWarningSettingBo.setLastBy(userBy);
		deviceWarningSettingBo.setLastDate(curr);
		return iDeviceWarningSettingService.edit(deviceWarningSettingBo);
	}

	@Override
	public List<DeviceWarningSettingVo> list(DeviceWarningSettingForm deviceWarningSettingForm) {
		DeviceWarningSettingBo deviceWarningSettingBo = new DeviceWarningSettingBo();
		BeanUtils.copyProperties(deviceWarningSettingForm,deviceWarningSettingBo);
		return iDeviceWarningSettingService.list(deviceWarningSettingBo);
	}

}
