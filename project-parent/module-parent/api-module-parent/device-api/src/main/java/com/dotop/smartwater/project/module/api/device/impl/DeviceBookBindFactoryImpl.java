package com.dotop.smartwater.project.module.api.device.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceBookBindFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBookBindBo;
import com.dotop.smartwater.project.module.core.water.form.DeviceBookBindForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBookBindVo;
import com.dotop.smartwater.project.module.service.device.IDeviceBookBindService;

/**
 * 

 * @description 表册绑定抄表员
 * @date 2019-10-23 08:54
 *
 */
@Component
public class DeviceBookBindFactoryImpl implements IDeviceBookBindFactory {

	@Autowired
	private IDeviceBookBindService iDeviceBookBindService;
	
	@Override
	public String configureDeviceBookBind(List<DeviceBookBindForm> deviceBookBindForms) {
		// TODO Auto-generated method stub
		UserVo user = AuthCasClient.getUser();
		Date curr = new Date();
		List<DeviceBookBindBo> deviceBookBindBos = new ArrayList<DeviceBookBindBo>();
		for(DeviceBookBindForm deviceBookBindForm: deviceBookBindForms) {
			DeviceBookBindBo deviceBookBindBo = BeanUtils.copy(deviceBookBindForm, DeviceBookBindBo.class);
			deviceBookBindBo.setId(UuidUtils.getUuid());
			deviceBookBindBo.setBindTime(curr);
			deviceBookBindBo.setEnterpriseid(user.getEnterpriseid());
			deviceBookBindBos.add(deviceBookBindBo);
		}
		return iDeviceBookBindService.configureDeviceBookBind(deviceBookBindBos);
	}

	@Override
	public List<DeviceBookBindVo> listDeviceBookBind(DeviceBookBindForm deviceBookBindForm) {
		// TODO Auto-generated method stub
		UserVo user = AuthCasClient.getUser();
		DeviceBookBindBo deviceBookBindBo = BeanUtils.copy(deviceBookBindForm, DeviceBookBindBo.class);
		deviceBookBindBo.setEnterpriseid(user.getEnterpriseid());
		return iDeviceBookBindService.listDeviceBookBind(deviceBookBindBo);
	}

	@Override
	public String deleteDeviceBookBind(DeviceBookBindForm deviceBookBindForm) {
		// TODO Auto-generated method stub
		UserVo user = AuthCasClient.getUser();
		DeviceBookBindBo deviceBookBindBo = BeanUtils.copy(deviceBookBindForm, DeviceBookBindBo.class);
		deviceBookBindBo.setEnterpriseid(user.getEnterpriseid());
		return iDeviceBookBindService.deleteDeviceBookBind(deviceBookBindBo);
	}

	@Override
	public Pagination<DeviceBookBindVo> pageBindOwner(DeviceBookBindForm deviceBookBindForm) {
		// TODO Auto-generated method stub
		// 获取用户信息
        UserVo user = AuthCasClient.getUser();
		DeviceBookBindBo deviceBookBindBo = BeanUtils.copy(deviceBookBindForm, DeviceBookBindBo.class);
		deviceBookBindBo.setEnterpriseid(user.getEnterpriseid());
		return iDeviceBookBindService.pageBindOwner(deviceBookBindBo);
	}

	@Override
	public Pagination<DeviceBookBindVo> listBindOwner(DeviceBookBindForm deviceBookBindForm) {
		// TODO Auto-generated method stub
		// 获取用户信息
        UserVo user = AuthCasClient.getUser();
		DeviceBookBindBo deviceBookBindBo = BeanUtils.copy(deviceBookBindForm, DeviceBookBindBo.class);
		deviceBookBindBo.setEnterpriseid(user.getEnterpriseid());
		return iDeviceBookBindService.listBindOwner(deviceBookBindBo);
	}

	@Override
	public String bindOwner(List<DeviceBookBindForm> deviceBookBindForms) {
		// TODO Auto-generated method stub
		// 获取用户信息
        UserVo user = AuthCasClient.getUser();
        List<DeviceBookBindBo> deviceBookBindBos = new ArrayList<DeviceBookBindBo>();
        for(DeviceBookBindForm deviceBookBindForm: deviceBookBindForms) {
    		DeviceBookBindBo deviceBookBindBo = BeanUtils.copy(deviceBookBindForm, DeviceBookBindBo.class);
    		deviceBookBindBo.setEnterpriseid(user.getEnterpriseid());
    		deviceBookBindBos.add(deviceBookBindBo);
        }
		return iDeviceBookBindService.bindOwner(deviceBookBindBos);
	}

	@Override
	public String deleteBindOwner(DeviceBookBindForm deviceBookBindForm) {
		// TODO Auto-generated method stub
		// 获取用户信息
        UserVo user = AuthCasClient.getUser();
		DeviceBookBindBo deviceBookBindBo = BeanUtils.copy(deviceBookBindForm, DeviceBookBindBo.class);
		deviceBookBindBo.setEnterpriseid(user.getEnterpriseid());
		return iDeviceBookBindService.deleteBindOwner(deviceBookBindBo);
	}

}
