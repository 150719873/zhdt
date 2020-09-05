package com.dotop.pipe.api.dao.alarm;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.dotop.pipe.core.dto.alarm.AlarmWMSettingDto;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.dependence.core.common.BaseVo;

public interface IAlarmWMSettingDao extends BaseDao<AlarmWMSettingDto, BaseVo> {

	public void add(AlarmWMSettingDto alarmDto) throws DataAccessException;

	public List<DeviceVo> getPage(AlarmWMSettingDto alarmWMSettingDto);

	Integer edit(AlarmWMSettingDto alarmDto);

	public List<DeviceVo> getWmAlarm(AlarmWMSettingDto alarmSettingDto);
}
