package com.dotop.pipe.api.dao.alarm;

import com.dotop.pipe.core.dto.alarm.AlarmSettingTemplateDto;
import com.dotop.pipe.core.vo.alarm.AlarmSettingTemplateVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface IAlarmSettingTemplateDao extends BaseDao<AlarmSettingTemplateDto, AlarmSettingTemplateVo> {

    void add(AlarmSettingTemplateDto alarmSettingDto) throws DataAccessException;

    Integer del(AlarmSettingTemplateDto alarmSettingDto) throws DataAccessException;

    List<AlarmSettingTemplateVo> lists(AlarmSettingTemplateDto alarmSettingDto) throws DataAccessException;

    AlarmSettingTemplateVo get(AlarmSettingTemplateDto alarmSettingDto) throws DataAccessException;

}
