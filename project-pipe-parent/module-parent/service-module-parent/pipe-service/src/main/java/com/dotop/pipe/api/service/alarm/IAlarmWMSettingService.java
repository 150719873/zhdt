package com.dotop.pipe.api.service.alarm;

import java.util.List;

import com.dotop.pipe.core.bo.alarm.AlarmWMSettingBo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

// 报警
public interface IAlarmWMSettingService extends BaseService<AlarmWMSettingBo, BaseVo> {

	BaseVo add(AlarmWMSettingBo alarmWMSettingBo);

	Pagination<DeviceVo> getPage(AlarmWMSettingBo alarmWMSettingBo);

	BaseVo edit(AlarmWMSettingBo alarmWMSettingBo);

	List<DeviceVo> getWmAlarm(AlarmWMSettingBo alarmWMSettingBo);
}
