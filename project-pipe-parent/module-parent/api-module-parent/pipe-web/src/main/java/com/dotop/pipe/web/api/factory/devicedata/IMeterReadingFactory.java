package com.dotop.pipe.web.api.factory.devicedata;

import com.dotop.pipe.core.form.AreaForm;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface IMeterReadingFactory extends BaseFactory<DeviceForm, DeviceVo> {

	/**
	 * 关联设备属性表 并查出最新的属性值
	 * 
	 * @param deviceForm
	 * @return
	 */
	Pagination<DeviceVo> devicePage(DeviceForm deviceForm);

	Pagination<DeviceVo> areaPage(AreaForm areaForm);
}
