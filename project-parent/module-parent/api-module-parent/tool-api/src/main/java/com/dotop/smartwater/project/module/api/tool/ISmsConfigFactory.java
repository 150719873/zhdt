package com.dotop.smartwater.project.module.api.tool;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.SmsConfigForm;
import com.dotop.smartwater.project.module.core.water.vo.SmsConfigVo;

/**
 * 短信配置
 * 

 * @date 2019年2月23日
 */
public interface ISmsConfigFactory extends BaseFactory<SmsConfigForm, SmsConfigVo> {

	@Override
	public Pagination<SmsConfigVo> page(SmsConfigForm smsConfigForm);

	@Override
	public SmsConfigVo get(SmsConfigForm smsConfigForm);

	@Override
	public SmsConfigVo add(SmsConfigForm smsConfigForm);

	@Override
	public String del(SmsConfigForm smsConfigForm);

}
