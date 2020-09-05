package com.dotop.smartwater.project.auth.api.impl;

import com.dotop.smartwater.project.auth.api.IDeviceChangeFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.auth.service.IDeviceChangeService;
import com.dotop.smartwater.project.module.core.water.bo.DeviceChangeBo;
import com.dotop.smartwater.project.module.core.water.form.DeviceChangeForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceChangeVo;

@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
public class DeviceChangeFactoryImpl implements IDeviceChangeFactory {

	@Autowired
	private IDeviceChangeService service;
	
	@Override
	public Pagination<DeviceChangeVo> page(DeviceChangeForm form) throws FrameworkRuntimeException {
		DeviceChangeBo bo = new DeviceChangeBo();
		BeanUtils.copyProperties(form, bo);
		return service.page(bo);
	}

}
