package com.dotop.pipe.web.api.factory.alarm;

import java.util.List;

import com.dotop.pipe.core.form.AlarmWMSettingForm;
import com.dotop.pipe.core.vo.alarm.AlarmWMSettingVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface IAlarmWMSettingFactory extends BaseFactory<AlarmWMSettingForm, BaseVo> {

	BaseVo add(AlarmWMSettingForm alarmWMSettingForm);

	Pagination<DeviceVo> getPage(AlarmWMSettingForm alarmWMSettingForm);

	BaseVo edit(AlarmWMSettingForm alarmWMSettingForm);

	List<AlarmWMSettingVo> getByDeviceId(AlarmWMSettingForm alarmWMSettingForm);

	List<DeviceVo> getAlarm(AlarmWMSettingForm alarmWMSettingForm);

}
