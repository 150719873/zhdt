package com.dotop.smartwater.project.module.api.tool;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.EmailSettingForm;
import com.dotop.smartwater.project.module.core.water.vo.EmailSettingVo;

public interface IEmailSettingFactory extends BaseFactory<EmailSettingForm, EmailSettingVo> {

	@Override
	Pagination<EmailSettingVo> page(EmailSettingForm emailSettingForm);

	Integer insertOrUpdate(EmailSettingForm emailSettingForm);
}
