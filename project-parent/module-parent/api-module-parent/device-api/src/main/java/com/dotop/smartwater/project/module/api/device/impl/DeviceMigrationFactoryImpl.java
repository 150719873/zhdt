package com.dotop.smartwater.project.module.api.device.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceMigrationFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceMigrationBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceMigrationHistoryBo;
import com.dotop.smartwater.project.module.core.water.form.DeviceMigrationForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceMigrationHistoryForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceMigrationHistoryVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceMigrationVo;
import com.dotop.smartwater.project.module.service.device.IDeviceMigrationService;

/**
 * 设备迁移

 * @date 2019-08-09
 *
 */
@Component
public class DeviceMigrationFactoryImpl implements IDeviceMigrationFactory {

	@Autowired
	private IDeviceMigrationService iDeviceMigrationService;
	
	@Override
	public List<DeviceMigrationVo> tempPage(DeviceMigrationForm deviceMigrationForm) {
		// TODO Auto-generated method stub
		AuthCasClient.getUser();

		DeviceMigrationBo deviceMigrationBo = new DeviceMigrationBo();
		BeanUtils.copyProperties(deviceMigrationForm, deviceMigrationBo);

		return iDeviceMigrationService.tempPage(deviceMigrationBo);
	}

	@Override
	public List<DeviceMigrationVo> pageMigration(DeviceMigrationForm deviceMigrationForm) {
		// TODO Auto-generated method stub
		AuthCasClient.getUser();

		DeviceMigrationBo deviceMigrationBo = new DeviceMigrationBo();
		deviceMigrationBo.setEnterpriseid(deviceMigrationForm.getEnterpriseid());
		if(deviceMigrationForm.getDevnoList() != null && !deviceMigrationForm.getDevnoList().isEmpty()) {
			deviceMigrationBo.setDevnoList(deviceMigrationForm.getDevnoList());
		}
		if(!StringUtils.isBlank(deviceMigrationForm.getBatchNo())) {
			String[] list = deviceMigrationForm.getBatchNo().split(",");
			deviceMigrationBo.setBatchnoList(new ArrayList<String>());
			for(String item: list) {
				deviceMigrationBo.getBatchnoList().add(item);
			}
		}
		if(!StringUtils.isBlank(deviceMigrationForm.getDevno())) {
			String[] list = deviceMigrationForm.getDevno().split(",");
			if(deviceMigrationBo.getDevnoList() == null || deviceMigrationBo.getDevnoList().isEmpty()) {
				deviceMigrationBo.setDevnoList(new ArrayList<String>());
			}
			deviceMigrationBo.setDevnoList(new ArrayList<String>());
			for(String item: list) {
				deviceMigrationBo.getDevnoList().add(item);
			}
		}

		return iDeviceMigrationService.pageMigration(deviceMigrationBo);
	}
	
	@Override
	public Pagination<DeviceMigrationVo> detail(DeviceMigrationForm deviceMigrationForm) {
		// TODO Auto-generated method stub
		AuthCasClient.getUser();

		DeviceMigrationBo deviceMigrationBo = new DeviceMigrationBo();
		BeanUtils.copyProperties(deviceMigrationForm, deviceMigrationBo);
		
		return iDeviceMigrationService.detail(deviceMigrationBo);
	}

	@Override
	public Integer clearTemp(DeviceMigrationForm deviceMigrationForm) {
		// TODO Auto-generated method stub
		AuthCasClient.getUser();

		DeviceMigrationBo deviceMigrationBo = new DeviceMigrationBo();
		BeanUtils.copyProperties(deviceMigrationForm, deviceMigrationBo);

		return iDeviceMigrationService.clearTemp(deviceMigrationBo);
	}

	@Override
	public Pagination<DeviceMigrationHistoryVo> pageHistory(DeviceMigrationHistoryForm deviceMigrationHistoryForm) {
		// TODO Auto-generated method stub
		AuthCasClient.getUser();

		DeviceMigrationHistoryBo deviceMigrationHistoryBo = new DeviceMigrationHistoryBo();
		BeanUtils.copyProperties(deviceMigrationHistoryForm, deviceMigrationHistoryBo);

		return iDeviceMigrationService.pageHistory(deviceMigrationHistoryBo);
	}

	@Override
	public String migrationDevice(DeviceMigrationHistoryForm deviceMigrationHistoryForm) {
		// TODO Auto-generated method stub
		UserVo user = AuthCasClient.getUser();
		Date curr = new Date();
		DeviceMigrationHistoryBo deviceMigrationHistoryBo = new DeviceMigrationHistoryBo();
		BeanUtils.copyProperties(deviceMigrationHistoryForm, deviceMigrationHistoryBo);
		deviceMigrationHistoryBo.setId(UuidUtils.getUuid());
		deviceMigrationHistoryBo.setMigrationUserId(user.getUserid());
		deviceMigrationHistoryBo.setMigrationTime(curr);

		return iDeviceMigrationService.migrationDevice(deviceMigrationHistoryBo);
	}

	@Override
	public String deleteHistory(DeviceMigrationHistoryForm deviceMigrationHistoryForm) {
		// TODO Auto-generated method stub
		AuthCasClient.getUser();

		DeviceMigrationHistoryBo deviceMigrationHistoryBo = new DeviceMigrationHistoryBo();
		BeanUtils.copyProperties(deviceMigrationHistoryForm, deviceMigrationHistoryBo);

		return iDeviceMigrationService.deleteHistory(deviceMigrationHistoryBo);
	}

	@Override
	public Integer updateTemp(DeviceMigrationForm deviceMigrationForm) {
		// TODO Auto-generated method stub
		AuthCasClient.getUser();

		DeviceMigrationBo deviceMigrationBo = new DeviceMigrationBo();
		BeanUtils.copyProperties(deviceMigrationForm, deviceMigrationBo);

		return iDeviceMigrationService.updateTemp(deviceMigrationBo);
	}

}
