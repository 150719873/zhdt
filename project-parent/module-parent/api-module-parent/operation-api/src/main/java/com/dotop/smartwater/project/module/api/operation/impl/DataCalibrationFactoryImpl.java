package com.dotop.smartwater.project.module.api.operation.impl;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.operation.IDataCalibrationFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DataCalibrationBo;
import com.dotop.smartwater.project.module.core.water.form.DataCalibrationForm;
import com.dotop.smartwater.project.module.core.water.vo.DataCalibrationVo;
import com.dotop.smartwater.project.module.service.operation.IDataCalibrationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 数据校准
 *

 * @date 2019/3/5.
 */
@Component
public class DataCalibrationFactoryImpl implements IDataCalibrationFactory {

	@Autowired
	private IDataCalibrationService iDataCalibrationService;

	@Override
	public Pagination<DataCalibrationVo> page(DataCalibrationForm dataCalibrationForm) {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();

		DataCalibrationBo dataCalibrationBo = new DataCalibrationBo();
		BeanUtils.copyProperties(dataCalibrationForm, dataCalibrationBo);

		dataCalibrationBo.setEnterpriseid(user.getEnterpriseid());
		dataCalibrationBo.setCreateBy(userBy);
		dataCalibrationBo.setCreateDate(curr);

		return iDataCalibrationService.page(dataCalibrationBo);
	}

	@Override
	public DataCalibrationVo get(DataCalibrationForm dataCalibrationForm) {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();

		DataCalibrationBo dataCalibrationBo = new DataCalibrationBo();
		BeanUtils.copyProperties(dataCalibrationForm, dataCalibrationBo);

		dataCalibrationBo.setEnterpriseid(user.getEnterpriseid());
		dataCalibrationBo.setCreateBy(userBy);
		dataCalibrationBo.setCreateDate(curr);

		return iDataCalibrationService.get(dataCalibrationBo);
	}

	@Override
	public DataCalibrationVo edit(DataCalibrationForm dataCalibrationForm) {
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();

		DataCalibrationBo dataCalibrationBo = new DataCalibrationBo();
		BeanUtils.copyProperties(dataCalibrationForm, dataCalibrationBo);

		if (StringUtils.isEmpty(dataCalibrationForm.getId())) {
			dataCalibrationBo.setId(UuidUtils.getUuid());
			dataCalibrationBo.setCreateBy(userBy);
			dataCalibrationBo.setCreateDate(curr);
		}

		dataCalibrationBo.setEnterpriseid(user.getEnterpriseid());

		return iDataCalibrationService.edit(dataCalibrationBo);
	}
}
