package com.dotop.smartwater.project.module.api.tool;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.EmailTemplateForm;
import com.dotop.smartwater.project.module.core.water.vo.EmailTemplateVo;

public interface IEmailTemplateFactory extends BaseFactory<EmailTemplateForm, EmailTemplateVo> {

	@Override
	Pagination<EmailTemplateVo> page(EmailTemplateForm emailTemplateForm);

	Integer insertOrUpdate(EmailTemplateForm emailTemplateForm);
}
