package com.dotop.smartwater.project.module.service.tool;

import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.common.BaseVo;

public interface ISmsService extends BaseService<BaseBo, BaseVo> {

	void sendWeChatMsg(String enterpriseid, int smstype, Map<String, String> params);

	void sendSMS(String enterpriseid, int model, String[] phoneNumbers, Object[] objects);
}
