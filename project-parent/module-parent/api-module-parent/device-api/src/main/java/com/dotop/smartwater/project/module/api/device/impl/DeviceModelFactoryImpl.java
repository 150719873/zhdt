package com.dotop.smartwater.project.module.api.device.impl;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceModelFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceModelBo;
import com.dotop.smartwater.project.module.core.water.constants.AppCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceModelForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceModelVo;
import com.dotop.smartwater.project.module.service.device.IDeviceModelService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**

 * @date 2019/2/26.
 */
@Component
public class DeviceModelFactoryImpl implements IDeviceModelFactory {

	private final static Logger logger = LogManager.getLogger(DeviceModelFactoryImpl.class);

	@Autowired
	private IDeviceModelService iDeviceModelService;

	@Override
	public Pagination<DeviceModelVo> find(DeviceModelForm deviceModelForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		DeviceModelBo deviceModelBo = new DeviceModelBo();
		BeanUtils.copyProperties(deviceModelForm, deviceModelBo);
		deviceModelBo.setEnterpriseid(user.getEnterpriseid());
		deviceModelBo.setUserBy(userBy);
		deviceModelBo.setCurr(curr);
		return iDeviceModelService.find(deviceModelBo);
	}

	@Override
	public List<DeviceModelVo> noPagingfind(DeviceModelForm deviceModelForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		DeviceModelBo deviceModelBo = new DeviceModelBo();
		BeanUtils.copyProperties(deviceModelForm, deviceModelBo);
		deviceModelBo.setEnterpriseid(user.getEnterpriseid());
		deviceModelBo.setUserBy(userBy);
		deviceModelBo.setCurr(curr);
		return iDeviceModelService.noPagingfind(deviceModelBo);
	}

	@Override
	public void save(DeviceModelForm deviceModelForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		DeviceModelBo deviceModelBo = new DeviceModelBo();
		BeanUtils.copyProperties(deviceModelForm, deviceModelBo);
		deviceModelBo.setEnterpriseid(user.getEnterpriseid());
		deviceModelBo.setUserBy(userBy);
		deviceModelBo.setCurr(curr);
		deviceModelBo.setCreatetime(DateUtils.format(new Date(), DateUtils.DATETIME));
		deviceModelBo.setCreateuser(user.getUserid());
		deviceModelBo.setUsername(user.getName());
		iDeviceModelService.save(deviceModelBo);
	}

	@Override
	public void update(DeviceModelForm deviceModelForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		DeviceModelBo deviceModelBo = new DeviceModelBo();
		BeanUtils.copyProperties(deviceModelForm, deviceModelBo);
		deviceModelBo.setEnterpriseid(user.getEnterpriseid());
		deviceModelBo.setUserBy(userBy);
		deviceModelBo.setCurr(curr);
		deviceModelBo.setCreatetime(DateUtils.format(new Date(), DateUtils.DATETIME));
		deviceModelBo.setCreateuser(user.getUserid());
		deviceModelBo.setUsername(user.getName());
		iDeviceModelService.update(deviceModelBo);
	}

	@Override
	public void delete(DeviceModelForm deviceModelForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		DeviceModelBo deviceModelBo = new DeviceModelBo();
		BeanUtils.copyProperties(deviceModelForm, deviceModelBo);
		deviceModelBo.setEnterpriseid(user.getEnterpriseid());
		deviceModelBo.setUserBy(userBy);
		deviceModelBo.setCurr(curr);
		int count = 0;
		if (StringUtils.isNotBlank(deviceModelForm.getId())) {
			// TODO
			// count = iOwnerService.checkTypeisUse("modelid", deviceModelForm.getId());
		}

		if (count > 0) {
			throw new FrameworkRuntimeException(AppCode.DEVEUI_IS_BAND, "当前水表型号已使用，无法删除");
		}

		iDeviceModelService.delete(deviceModelBo);
	}

	@Override
	public DeviceModelVo get(DeviceModelForm deviceModelForm) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		DeviceModelBo deviceModelBo = new DeviceModelBo();
		BeanUtils.copyProperties(deviceModelForm, deviceModelBo);
		deviceModelBo.setEnterpriseid(user.getEnterpriseid());
		deviceModelBo.setUserBy(userBy);
		deviceModelBo.setCurr(curr);
		return iDeviceModelService.get(deviceModelBo);
	}
}
