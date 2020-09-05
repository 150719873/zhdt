package com.dotop.pipe.web.api.factory.third;

import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface IThirdDeviceFactory extends BaseFactory<DeviceForm, DeviceVo> {

	Pagination<DeviceVo> page(DeviceForm DeviceForm) throws FrameworkRuntimeException;

}
