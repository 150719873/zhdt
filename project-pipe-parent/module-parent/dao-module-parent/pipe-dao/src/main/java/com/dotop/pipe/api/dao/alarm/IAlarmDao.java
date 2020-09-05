package com.dotop.pipe.api.dao.alarm;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.springframework.dao.DataAccessException;

import com.dotop.pipe.core.dto.alarm.AlarmDto;
import com.dotop.pipe.core.vo.alarm.AlarmVo;

public interface IAlarmDao extends BaseDao<AlarmDto, AlarmVo> {

    public List<AlarmVo> list(AlarmDto alarmDto) throws DataAccessException;

    public List<AlarmVo> areaList(AlarmDto alarmDto) throws DataAccessException;

    public AlarmVo get(AlarmDto alarmDto) throws DataAccessException;

    public AlarmVo getAreaAlarm(AlarmDto alarmDto) throws DataAccessException;

    public Integer edit(AlarmDto alarmDto) throws DataAccessException;

    public void add(AlarmDto alarmDto) throws DataAccessException;

    public List<AlarmVo> listByDevice(AlarmDto alarmDto) throws DataAccessException;

}
