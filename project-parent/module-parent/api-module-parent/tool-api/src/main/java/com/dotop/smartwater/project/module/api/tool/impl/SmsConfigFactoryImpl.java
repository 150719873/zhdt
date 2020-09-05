package com.dotop.smartwater.project.module.api.tool.impl;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.api.tool.ISmsConfigFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.SmsConfigBo;
import com.dotop.smartwater.project.module.core.water.form.SmsConfigForm;
import com.dotop.smartwater.project.module.core.water.vo.SmsConfigVo;
import com.dotop.smartwater.project.module.service.tool.ISmsConfigService;

/**
 * 短信配置
 * 

 * @date 2019年2月23日
 */

@Component
public class SmsConfigFactoryImpl implements ISmsConfigFactory {

	@Autowired
	private ISmsConfigService iSmsConfigService;

	@Override
	public Pagination<SmsConfigVo> page(SmsConfigForm smsConfigForm)  {
		SmsConfigBo smsConfigBo = new SmsConfigBo();
		BeanUtils.copyProperties(smsConfigForm, smsConfigBo);
		return iSmsConfigService.page(smsConfigBo);
	}

	@Override
	public SmsConfigVo get(SmsConfigForm smsConfigForm)  {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		SmsConfigBo smsConfigBo = new SmsConfigBo();
		smsConfigBo.setId(smsConfigForm.getId());
		smsConfigBo.setEnterpriseid(operEid);
		return iSmsConfigService.get(smsConfigBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public SmsConfigVo add(SmsConfigForm smsConfigForm)  {
		Date curr = new Date();
		// 复制属性
		SmsConfigBo smsConfigBo = new SmsConfigBo();
		BeanUtils.copyProperties(smsConfigForm, smsConfigBo);
		// 属性复制
		smsConfigBo.setUserBy("admin");
		smsConfigBo.setCurr(curr);
		return iSmsConfigService.add(smsConfigBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public SmsConfigVo edit(SmsConfigForm smsConfigForm)  {
		Date curr = new Date();
		// 复制属性
		SmsConfigBo smsConfigBo = new SmsConfigBo();
		BeanUtils.copyProperties(smsConfigForm, smsConfigBo);
		smsConfigBo.setUserBy("admin");
		smsConfigBo.setCurr(curr);
		return iSmsConfigService.edit(smsConfigBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(SmsConfigForm smsConfigForm)  {
		String id = smsConfigForm.getId();
		SmsConfigBo smsConfigBo = new SmsConfigBo();
		smsConfigBo.setId(id);
		iSmsConfigService.del(smsConfigBo);
		return id;
	}

}
