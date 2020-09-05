package com.dotop.smartwater.project.module.api.device.impl;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.project.module.api.device.IDeviceBookManagementFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBookManagementBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceBookManagementForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBookManagementVo;
import com.dotop.smartwater.project.module.service.device.IDeviceBookManagementService;

/**
 * 字典配置
 *

 * @date 2019年2月23日
 */

@Component
public class DeviceBookManagementFactoryImpl implements IDeviceBookManagementFactory, IAuthCasClient {

    @Autowired
    private IDeviceBookManagementService iDeviceBookManagementService;


    @Override
    public List<DeviceBookManagementVo> list(DeviceBookManagementForm deviceBookManagementForm) throws FrameworkRuntimeException {
    	UserVo user = AuthCasClient.getUser();
        DeviceBookManagementBo bo = BeanUtils.copy(deviceBookManagementForm, DeviceBookManagementBo.class);
        bo.setEnterpriseid(user.getEnterpriseid());
        return iDeviceBookManagementService.list(bo);
    }

    @Override
    public DeviceBookManagementVo get(DeviceBookManagementForm deviceBookManagementForm) throws FrameworkRuntimeException {
    	UserVo user = AuthCasClient.getUser();
    	DeviceBookManagementBo bo = BeanUtils.copy(deviceBookManagementForm, DeviceBookManagementBo.class);
    	bo.setEnterpriseid(user.getEnterpriseid());
        return iDeviceBookManagementService.get(bo);
    }

    @Override
    public DeviceBookManagementVo add(DeviceBookManagementForm deviceBookManagementForm) throws FrameworkRuntimeException {
        // 获取用户信息
        UserVo user = AuthCasClient.getUser();
        Date date = new Date();

        DeviceBookManagementBo deviceBookManagementBo = new DeviceBookManagementBo();
        deviceBookManagementBo.setBookNum(deviceBookManagementForm.getBookNum());
        deviceBookManagementBo.setEnterpriseid(getEnterpriseid());
        if (iDeviceBookManagementService.isExist(deviceBookManagementBo)) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "该表册号已经存在");
        }
        deviceBookManagementBo = BeanUtils.copy(deviceBookManagementForm, DeviceBookManagementBo.class);
        deviceBookManagementBo.setCreateDate(date);
        deviceBookManagementBo.setCreateBy(user.getUserid());
        deviceBookManagementBo.setLastDate(date);
        deviceBookManagementBo.setLastBy(user.getUserid());
        deviceBookManagementBo.setEnterpriseid(user.getEnterpriseid());
        return iDeviceBookManagementService.add(deviceBookManagementBo);
    }

    @Override
    public DeviceBookManagementVo edit(DeviceBookManagementForm deviceBookManagementForm) throws FrameworkRuntimeException {
        // 获取用户信息
        UserVo user = AuthCasClient.getUser();
        DeviceBookManagementBo deviceBookManagementBo = BeanUtils.copy(deviceBookManagementForm, DeviceBookManagementBo.class);
        deviceBookManagementBo.setLastBy(user.getUserid());
        deviceBookManagementBo.setLastDate(new Date());
        deviceBookManagementBo.setEnterpriseid(getEnterpriseid());
        return iDeviceBookManagementService.edit(deviceBookManagementBo);
    }

    @Override
    public String del(DeviceBookManagementForm deviceBookManagementForm) throws FrameworkRuntimeException {
    	// 获取用户信息
        UserVo user = AuthCasClient.getUser();
    	DeviceBookManagementBo deviceBookManagementBo = new DeviceBookManagementBo();
        deviceBookManagementBo.setBookId(deviceBookManagementForm.getBookId());
        deviceBookManagementBo.setEnterpriseid(user.getEnterpriseid());
        if(iDeviceBookManagementService.judgeIfExistWorker(deviceBookManagementBo) == 0
        		&& iDeviceBookManagementService.judgeIfExistOwner(deviceBookManagementBo) == 0) {
        	return iDeviceBookManagementService.del(deviceBookManagementBo);
        }else {
        	return "Fail";
        }
        
    }

}
