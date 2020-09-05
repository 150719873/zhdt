package com.dotop.pipe.web.api.factory.device;

import com.dotop.pipe.core.form.DeviceUpDownStreamForm;
import com.dotop.pipe.core.vo.device.DeviceUpDownStreamVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface IDeviceUpDownStreamFactory extends BaseFactory<DeviceUpDownStreamForm, DeviceUpDownStreamVo> {

	DeviceUpDownStreamVo add(DeviceUpDownStreamForm deviceUpDownStreamForm);

	void editAlarmProperty(DeviceUpDownStreamForm deviceUpDownStreamForm);

	String del(DeviceUpDownStreamForm deviceUpDownStreamForm);

	DeviceUpDownStreamVo get(DeviceUpDownStreamForm deviceUpDownStreamForm);

	Pagination<DeviceUpDownStreamVo> page(DeviceUpDownStreamForm deviceUpDownStreamForm);
}
