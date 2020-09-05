package com.dotop.smartwater.project.module.api.device;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.DeviceWarningSettingForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningSettingVo;

import java.util.List;

/**
 * 设备预警设置

 * @date 2019/4/1.
 */
public interface IDeviceWarningSettingFactory extends BaseFactory<DeviceWarningSettingForm, DeviceWarningSettingVo> {

	@Override
	Pagination<DeviceWarningSettingVo> page(DeviceWarningSettingForm deviceWarningSettingForm);

	@Override
	DeviceWarningSettingVo edit(DeviceWarningSettingForm deviceWarningSettingForm);


	@Override
	List<DeviceWarningSettingVo> list(DeviceWarningSettingForm deviceWarningSettingForm);
}
