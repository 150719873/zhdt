package com.dotop.smartwater.project.module.api.tool.impl;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.project.module.api.tool.IDeviceParametersFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.api.revenue.INumRuleSetFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceParametersBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceParametersForm;
import com.dotop.smartwater.project.module.core.water.form.customize.MakeNumRequest;
import com.dotop.smartwater.project.module.core.water.vo.DeviceParametersVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.MakeNumVo;
import com.dotop.smartwater.project.module.service.tool.IDeviceParametersService;

/**
 * 

 * @date 2019年2月21日
 */

@Component
public class DeviceParametersFactoryImpl implements IDeviceParametersFactory {

	@Autowired
	private IDeviceParametersService iDeviceParametersService;
	
	@Autowired
	private INumRuleSetFactory iNumRuleSetFactory;

	@Override
	public Pagination<DeviceParametersVo> page(DeviceParametersForm deviceParametersForm)
			 {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		DeviceParametersBo deviceParametersBo = new DeviceParametersBo();
		BeanUtils.copyProperties(deviceParametersForm, deviceParametersBo);
		deviceParametersBo.setEnterpriseid(operEid);
		return iDeviceParametersService.page(deviceParametersBo);
	}

	@Override
	public DeviceParametersVo get(DeviceParametersForm deviceParametersForm)  {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		DeviceParametersBo deviceParametersBo = new DeviceParametersBo();
		deviceParametersBo.setDeviceParId(deviceParametersForm.getDeviceParId());
		deviceParametersBo.setEnterpriseid(operEid);
		return iDeviceParametersService.get(deviceParametersBo);
	}
	
	public DeviceParametersVo getParams(DeviceParametersForm deviceParametersForm) {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		DeviceParametersBo deviceParametersBo = new DeviceParametersBo();
		deviceParametersBo.setDeviceParId(deviceParametersForm.getDeviceParId());
		deviceParametersBo.setEnterpriseid(operEid);
		return iDeviceParametersService.getParams(deviceParametersBo);
	}
	

	
	public boolean checkDeviceName(DeviceParametersForm deviceParametersForm) {
		UserVo user = AuthCasClient.getUser();
		// 复制属性
		DeviceParametersBo deviceParametersBo = new DeviceParametersBo();
		BeanUtils.copyProperties(deviceParametersForm, deviceParametersBo);
		deviceParametersBo.setEnterpriseid(user.getEnterpriseid());
		deviceParametersBo.setUserBy(user.getName());
		deviceParametersBo.setCurr(new Date());
		return iDeviceParametersService.checkDeviceName(deviceParametersBo);
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DeviceParametersVo add(DeviceParametersForm deviceParametersForm)  {
		UserVo user = AuthCasClient.getUser();
		// 复制属性
		DeviceParametersBo deviceParametersBo = new DeviceParametersBo();
		BeanUtils.copyProperties(deviceParametersForm, deviceParametersBo);
		deviceParametersBo.setEnterpriseid(user.getEnterpriseid());
		deviceParametersBo.setUserBy(user.getName());
		deviceParametersBo.setCurr(new Date());
		
		// 生成样品批次号
		MakeNumRequest make = new MakeNumRequest();
		make.setRuleid(23);
		make.setCount(1);
		make.setEnterpriseid(user.getEnterpriseid());
		MakeNumVo vo = iNumRuleSetFactory.wechatMakeNo(make);
		if (vo != null && vo.getNumbers() != null && vo.getNumbers().size() > 0) {
			deviceParametersBo.setSerialNumber(vo.getNumbers().get(0));
		} else {
			deviceParametersBo.setSerialNumber(String.valueOf(Config.Generator.nextId()));
		}
		
		DeviceParametersVo deviceParametersVo = null;
		// 校验名称是否存在
		Boolean flag = iDeviceParametersService.isExist(deviceParametersBo);
		if (flag) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "设备参数名称已存在");
		} else {
			deviceParametersVo = iDeviceParametersService.add(deviceParametersBo);
		}
		return deviceParametersVo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DeviceParametersVo edit(DeviceParametersForm deviceParametersForm)  {
		UserVo user = AuthCasClient.getUser();
		Date curr = new Date();
		String userBy = user.getName();
		String operEid = user.getEnterpriseid();
		// 复制属性
		DeviceParametersBo deviceParametersBo = new DeviceParametersBo();
		BeanUtils.copyProperties(deviceParametersForm, deviceParametersBo);
		deviceParametersBo.setEnterpriseid(operEid);
		deviceParametersBo.setUserBy(userBy);
		deviceParametersBo.setCurr(curr);
		return iDeviceParametersService.edit(deviceParametersBo);
	}

	@Override
	public List<DeviceParametersVo> list(DeviceParametersForm deviceParametersForm)  {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		DeviceParametersBo deviceParametersBo = new DeviceParametersBo();
		deviceParametersBo.setEnterpriseid(operEid);
		return iDeviceParametersService.list(deviceParametersBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(DeviceParametersForm deviceParametersForm)  {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		String deviceParId = deviceParametersForm.getDeviceParId();
		DeviceParametersBo deviceParametersBo = new DeviceParametersBo();
		deviceParametersBo.setDeviceParId(deviceParId);
		deviceParametersBo.setEnterpriseid(operEid);
		iDeviceParametersService.del(deviceParametersBo);
		return deviceParId;
	}

}
