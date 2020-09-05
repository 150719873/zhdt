package com.dotop.smartwater.project.module.service.tool;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.SmsTemplateBo;
import com.dotop.smartwater.project.module.core.water.vo.SmsTemplateVo;

public interface ISmsTemplateService extends BaseService<SmsTemplateBo, SmsTemplateVo> {

	Pagination<SmsTemplateVo> getPageList(SmsTemplateBo smsTemplate);

	int addSmsTemplate(SmsTemplateBo smsTemplate);

	SmsTemplateVo getSmsTemplateVo(SmsTemplateBo smsTemplate);

	SmsTemplateVo getBySmstype(String enterpriseid, Integer smstype);

	SmsTemplateVo getEnableByCode(String enterpriseid, Integer modeType);

	int updateSmsTemplate(SmsTemplateBo smsTemplate);

	int delete(String id);

	int updateStatus(String id, int status);

	SmsTemplateVo getByEnterpriseidAndType(String enterpriseid, Integer modeltype);
}
