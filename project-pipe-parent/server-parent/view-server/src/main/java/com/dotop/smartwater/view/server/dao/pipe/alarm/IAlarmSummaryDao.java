package com.dotop.smartwater.view.server.dao.pipe.alarm;


import com.dotop.pipe.core.dto.alarm.AlarmDto;
import com.dotop.pipe.core.vo.alarm.AlarmVo;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 *
 */
public interface IAlarmSummaryDao {

    public List<AlarmVo> list(AlarmDto alarmDto) throws DataAccessException;
}
