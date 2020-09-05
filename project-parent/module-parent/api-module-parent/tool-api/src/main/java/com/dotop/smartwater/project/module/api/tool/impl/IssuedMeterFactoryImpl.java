package com.dotop.smartwater.project.module.api.tool.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.dotop.smartwater.project.module.api.tool.IIssuedMeterFactory;
import com.dotop.smartwater.project.module.service.device.IDeviceBookManagementService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceBookManagementFactory;
import com.dotop.smartwater.project.module.api.device.IMeterReadingFactory;
import com.dotop.smartwater.project.module.api.revenue.INumRuleSetFactory;
import com.dotop.smartwater.project.module.api.revenue.IOwnerFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.form.DeviceBookManagementForm;
import com.dotop.smartwater.project.module.core.water.form.IssuedMeterForm;
import com.dotop.smartwater.project.module.core.water.form.MeterReadingDetailForm;
import com.dotop.smartwater.project.module.core.water.form.MeterReadingTaskForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.form.customize.MakeNumRequest;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBookManagementVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.MakeNumVo;

@Component
public class IssuedMeterFactoryImpl implements IIssuedMeterFactory {

	private static final Logger LOGGER = LogManager.getLogger(IssuedMeterFactoryImpl.class);

	@Autowired
	private INumRuleSetFactory iNumRuleSetFactory;

	@Autowired
	private IDeviceBookManagementFactory bookManagementFactory;

	@Autowired
	private IDeviceBookManagementService iDeviceBookManagementService;

	@Autowired
	private IOwnerFactory ownerFactory;

	@Autowired
	private IMeterReadingFactory iMeterReadingFactory;

	@Override
	public boolean generate(IssuedMeterForm form) {
		UserVo user = AuthCasClient.getUser();
		MeterReadingTaskForm tform = new MeterReadingTaskForm();

		// 生成批次号
		MakeNumRequest make = new MakeNumRequest();
		make.setRuleid(18);
		make.setCount(1);
		make.setEnterpriseid(form.getEnterpriseid());
		MakeNumVo vo = iNumRuleSetFactory.wechatMakeNo(make);
		if (vo != null && vo.getNumbers() != null && !vo.getNumbers().isEmpty()) {
			tform.setBatchId(vo.getNumbers().get(0));
		} else {
			tform.setBatchId(String.valueOf(Config.Generator.nextId()));
		}
		tform.setTaskName(form.getName());
		tform.setArea(form.getCommunityids());
		tform.setTaskStartTime(new Date());
		try {
			tform.setTaskEndTime(new SimpleDateFormat("yyyy-MM-dd").parse(form.getEndTime()));
		} catch (Exception e) {
			LOGGER.debug("exception", e);
		}

		String[] areas = form.getCommunityids().split(",");
		//获取抄表人员
		List<String> books = iDeviceBookManagementService.findReadersbyAreas(Arrays.asList(areas),user.getEnterpriseid());
		int numbers = books.size();

		for (String id : areas) {

			// 获取区域下业主信息
			OwnerForm oform = new OwnerForm();
			oform.setCommunityid(id);
			List<OwnerVo> list = ownerFactory.getCommunityOwner(oform);

			List<MeterReadingDetailForm> details = new ArrayList<>();
			for (OwnerVo owner : list) {
				MeterReadingDetailForm rform = new MeterReadingDetailForm();
				rform.setId(UuidUtils.getUuid());
				rform.setBatchId(tform.getBatchId());
				rform.setArea(id);
				rform.setUserCode(owner.getUserno());
				rform.setUserName(owner.getUsername());
				rform.setPhone(owner.getUserphone());
				rform.setAddress(owner.getUseraddr());
				rform.setMeterCode(owner.getDevno());
				rform.setMeterPurpose(owner.getPaytypeid());
				rform.setDeadline(tform.getTaskEndTime());
				rform.setStatus(0);
				rform.setEnterpriseid(user.getEnterpriseid());
				rform.setCreateBy(user.getName());
				rform.setCreateDate(new Date());
				details.add(rform);
			}
			if (!details.isEmpty()) {
				iMeterReadingFactory.batchAdd(details);
			}
		}
		tform.setReaderNum(numbers);
		tform.setStatus(0);
		tform.setEnterpriseid(user.getEnterpriseid());
		tform.setLastBy(user.getName());
		tform.setLastDate(new Date());
		iMeterReadingFactory.edit(tform);
		return true;
	}

}
