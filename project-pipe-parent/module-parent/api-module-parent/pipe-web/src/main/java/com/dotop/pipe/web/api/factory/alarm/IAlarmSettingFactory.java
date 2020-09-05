package com.dotop.pipe.web.api.factory.alarm;

import org.springframework.web.multipart.MultipartFile;

import com.dotop.pipe.core.form.AlarmSettingForm;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.vo.alarm.AlarmSettingVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface IAlarmSettingFactory extends BaseFactory<AlarmSettingForm, AlarmSettingVo> {

	/**
	 * 预警值设置新增
	 */
	AlarmSettingVo add(AlarmSettingForm alarmSettingForm);

	/**
	 * 设备预警设置分页查询
	 * 
	 * @param deviceForm
	 * @return
	 */
	Pagination<DeviceVo> page(DeviceForm deviceForm);

	/**
	 * 预警设置查询详情接口
	 */
	AlarmSettingVo get(AlarmSettingForm alarmSettingForm);

	/**
	 * 设备预警导入设置上传
	 * 
	 * @param file
	 * @return
	 */
	String importDate(MultipartFile file);
}
