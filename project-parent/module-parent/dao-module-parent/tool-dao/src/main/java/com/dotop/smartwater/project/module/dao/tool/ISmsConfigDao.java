package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.SmsConfigDto;
import com.dotop.smartwater.project.module.core.water.vo.SmsConfigVo;

import java.util.List;

/**
 * 短信配置
 *

 * @date 2019年2月23日
 */
public interface ISmsConfigDao extends BaseDao<SmsConfigDto, SmsConfigVo> {

	@Override
	List<SmsConfigVo> list(SmsConfigDto smsConfigDto);

	@Override
	SmsConfigVo get(SmsConfigDto smsConfigDto);

	@Override
	void add(SmsConfigDto smsConfigDto);

	@Override
	Integer edit(SmsConfigDto smsConfigDto);

	@Override
	Integer del(SmsConfigDto smsConfigDto);

	@Override
	Boolean isExist(SmsConfigDto smsConfigDto);

	SmsConfigVo getByEnable(SmsConfigDto smsConfigDto);
}
