package com.dotop.pipe.api.service.alarm;

import com.dotop.pipe.core.bo.alarm.AlarmNoticeBo;
import com.dotop.pipe.core.vo.alarm.AlarmNoticeRuleVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

// 报警
public interface IAlarmNoticeRuleService {

	/**
	 * 报警分页查询
	 * 
	 * @param enterpriseId
	 * @param page
	 * @param pageSize
	 * @param string
	 * 
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Pagination<AlarmNoticeRuleVo> page(AlarmNoticeBo alarmBo) throws FrameworkRuntimeException;

	AlarmNoticeRuleVo add(AlarmNoticeBo alarmBo) throws FrameworkRuntimeException;

	String del(AlarmNoticeBo alarmBo) throws FrameworkRuntimeException;

	 boolean isExist(AlarmNoticeBo alarmBo) throws FrameworkRuntimeException;

	public AlarmNoticeRuleVo edit(AlarmNoticeBo alarmBo) throws FrameworkRuntimeException;

	Pagination<AlarmNoticeRuleVo> logPage(AlarmNoticeBo alarmBo) throws FrameworkRuntimeException;

	AlarmNoticeRuleVo addLog(AlarmNoticeBo alarmBo) throws FrameworkRuntimeException;

}
