package com.dotop.smartwater.view.server.service.alarm;


import com.dotop.pipe.core.bo.alarm.AlarmBo;
import com.dotop.pipe.core.vo.alarm.AlarmVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 *
 */
public interface IAlarmSummaryService {


    Pagination<AlarmVo> pageAlarm(AlarmBo alarmBo) throws FrameworkRuntimeException;;
}
