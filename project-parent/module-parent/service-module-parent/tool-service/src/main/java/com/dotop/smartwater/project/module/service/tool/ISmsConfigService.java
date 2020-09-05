package com.dotop.smartwater.project.module.service.tool;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.SmsConfigBo;
import com.dotop.smartwater.project.module.core.water.vo.SmsConfigVo;

/**
 * 短信配置
 * 

 * @date 2019年2月23日
 */
public interface ISmsConfigService extends BaseService<SmsConfigBo, SmsConfigVo> {

	@Override
	Pagination<SmsConfigVo> page(SmsConfigBo smsConfigBo);

	@Override
	SmsConfigVo get(SmsConfigBo smsConfigBo);

	@Override
	SmsConfigVo add(SmsConfigBo smsConfigBo);

	@Override
	String del(SmsConfigBo smsConfigBo);

	@Override
	boolean isExist(SmsConfigBo smsConfigBo);

	SmsConfigVo getByEnable(SmsConfigBo smsConfigBo);
}
