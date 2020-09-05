package com.dotop.pipe.web.api.factory.alarm;

import com.dotop.pipe.core.form.AlarmNoticeForm;
import com.dotop.pipe.core.vo.alarm.AlarmNoticeRuleVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface IAlarmNoticeRuleFactory extends BaseFactory<AlarmNoticeForm, AlarmNoticeRuleVo> {

    /**
     * 报警分页查询
     *
     * @param page
     * @param pageSize
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<AlarmNoticeRuleVo> page(AlarmNoticeForm alarmNoticeForm) throws FrameworkRuntimeException;

    /**
     * 新增报警
     *
     * @return
     * @throws FrameworkRuntimeException
     */
    AlarmNoticeRuleVo add(AlarmNoticeForm alarmForm) throws FrameworkRuntimeException;

    String del(AlarmNoticeForm alarmForm) throws FrameworkRuntimeException;

    /**
     * 告警通知日志分页查询
     *
     * @param page
     * @param pageSize
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<AlarmNoticeRuleVo> logPage(AlarmNoticeForm alarmNoticeForm) throws FrameworkRuntimeException;
}
