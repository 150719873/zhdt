package com.dotop.pipe.api.client;

import com.dotop.pipe.core.bo.alarm.AlarmBo;
import com.dotop.pipe.core.vo.alarm.AlarmNoticeRuleVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

/**
 *
 */
public interface IWaterNoticeClient {

	void add(AlarmBo alarmBo, AlarmNoticeRuleVo alarmNoticeVo) throws FrameworkRuntimeException;
}
