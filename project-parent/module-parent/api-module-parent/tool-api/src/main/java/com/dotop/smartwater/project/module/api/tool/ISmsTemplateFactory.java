package com.dotop.smartwater.project.module.api.tool;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.SmsTemplateForm;
import com.dotop.smartwater.project.module.core.water.vo.SmsTemplateVo;

public interface ISmsTemplateFactory extends BaseFactory<SmsTemplateForm, SmsTemplateVo> {

	Pagination<SmsTemplateVo> getPageList(SmsTemplateForm smsTemplate);

	int addSmsTemplate(SmsTemplateForm smsTemplate);

	SmsTemplateVo getSmsTemplateVo(SmsTemplateForm smsTemplate);

	SmsTemplateVo getBySmstype(String enterpriseid, Integer smstype);

	SmsTemplateVo getEnableByCode(String enterpriseid, Integer modeType);

	int updateSmsTemplate(SmsTemplateForm smsTemplate);

	int delete(String id);

	int updateStatus(String id, int status);
}
