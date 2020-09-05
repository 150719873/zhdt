package com.dotop.smartwater.project.module.service.tool;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.EmailTemplateBo;
import com.dotop.smartwater.project.module.core.water.vo.EmailTemplateVo;

public interface IEmailTemplateService extends BaseService<EmailTemplateBo, EmailTemplateVo> {

	@Override
	Pagination<EmailTemplateVo> page(EmailTemplateBo emailTemplateBo);

	Integer insertOrUpdate(EmailTemplateBo emailTemplateBo);

	EmailTemplateVo getByEnterpriseAndType(String enterpriseid, Integer modeltype);
}
