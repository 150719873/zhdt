package com.dotop.smartwater.project.module.api.tool.impl;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.api.tool.IEmailSettingFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.EmailSettingBo;
import com.dotop.smartwater.project.module.core.water.form.EmailSettingForm;
import com.dotop.smartwater.project.module.core.water.vo.EmailSettingVo;
import com.dotop.smartwater.project.module.service.tool.IEmailSettingService;

@Component
public class EmailSettingFactoryImpl implements IEmailSettingFactory {

	@Resource
	private IEmailSettingService iEmailSettingService;

	@Override
	public Pagination<EmailSettingVo> page(EmailSettingForm emailSettingForm) {
		EmailSettingBo emailSettingBo = new EmailSettingBo();
		BeanUtils.copyProperties(emailSettingForm, emailSettingBo);
		return iEmailSettingService.page(emailSettingBo);
	}

	@Override
	public Integer insertOrUpdate(EmailSettingForm emailSettingForm) {
		UserVo user = AuthCasClient.getUser();
		EmailSettingBo emailSettingBo = new EmailSettingBo();
		BeanUtils.copyProperties(emailSettingForm, emailSettingBo);
		if (emailSettingBo.getId() == null) {
			emailSettingBo.setCreateuser(user.getUserid());
		}
		emailSettingBo.setUpdateuser(user.getUserid());
		return iEmailSettingService.insertOrUpdate(emailSettingBo);
	}

}
