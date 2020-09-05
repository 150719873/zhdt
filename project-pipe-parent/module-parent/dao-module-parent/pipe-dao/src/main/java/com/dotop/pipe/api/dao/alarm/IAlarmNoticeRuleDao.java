package com.dotop.pipe.api.dao.alarm;

import com.dotop.pipe.core.dto.alarm.AlarmNoticeDto;
import com.dotop.pipe.core.vo.alarm.AlarmNoticeRuleVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface IAlarmNoticeRuleDao extends BaseDao<AlarmNoticeDto, AlarmNoticeRuleVo> {

    public List<AlarmNoticeRuleVo> list(AlarmNoticeDto alarmDto) throws DataAccessException;

    public void add(AlarmNoticeDto alarmDto) throws DataAccessException;

    public Integer del(AlarmNoticeDto alarmDto) throws DataAccessException;

    /**
     * 告警日志分页查询
     * @param alarmDto
     * @return
     * @throws DataAccessException
     */
    public List<AlarmNoticeRuleVo> logList(AlarmNoticeDto alarmDto) throws DataAccessException;

    public void addLog(AlarmNoticeDto alarmDto) throws DataAccessException;

    @Override
    Boolean isExist(AlarmNoticeDto alarmDto) throws DataAccessException;

    @Override
    Integer edit(AlarmNoticeDto alarmDto) throws DataAccessException;

}
