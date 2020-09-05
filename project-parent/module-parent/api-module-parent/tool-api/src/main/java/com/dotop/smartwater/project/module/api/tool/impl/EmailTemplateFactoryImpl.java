package com.dotop.smartwater.project.module.api.tool.impl;

import com.dotop.smartwater.project.module.api.tool.IEmailTemplateFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.water.bo.EmailTemplateBo;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.EmailTemplateForm;
import com.dotop.smartwater.project.module.core.water.vo.EmailTemplateVo;
import com.dotop.smartwater.project.module.service.tool.IEmailTemplateService;

@Component
public class EmailTemplateFactoryImpl implements IEmailTemplateFactory, IAuthCasClient {

	@Autowired
	private IEmailTemplateService iEmailTemplateService;

	@Override
	public Pagination<EmailTemplateVo> page(EmailTemplateForm emailTemplateForm)  {
		if (getUsertype() == WaterConstants.USER_TYPE_ADMIN) {
			// 最高管理员admin
		} else {
			emailTemplateForm.setEnterpriseid(getEnterpriseid());
		}
		EmailTemplateBo emailTemplateBo = new EmailTemplateBo();
		BeanUtils.copyProperties(emailTemplateForm, emailTemplateBo);
		return iEmailTemplateService.page(emailTemplateBo);
	}

	@Override
	public Integer insertOrUpdate(EmailTemplateForm emailTemplateForm)  {
		EmailTemplateBo emailTemplateBo = new EmailTemplateBo();
		BeanUtils.copyProperties(emailTemplateForm, emailTemplateBo);

		if (getUsertype() == WaterConstants.USER_TYPE_ADMIN) {
			if (emailTemplateBo.getId() == null) {
				emailTemplateBo.setCreateuser("admin");
			}
			emailTemplateBo.setUpdateuser("admin");
		} else {
			emailTemplateBo.setEnterpriseid(getEnterpriseid());
			if (emailTemplateBo.getId() == null) {
				emailTemplateBo.setCreateuser(getName());
			}
			emailTemplateBo.setUpdateuser(getName());
		}

		return iEmailTemplateService.insertOrUpdate(emailTemplateBo);
	}

}
