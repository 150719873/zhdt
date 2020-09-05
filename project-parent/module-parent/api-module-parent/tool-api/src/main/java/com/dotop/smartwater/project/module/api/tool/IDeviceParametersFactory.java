package com.dotop.smartwater.project.module.api.tool;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.DeviceParametersForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceParametersVo;

import java.util.List;

/**
 * 设备参数绑定接口
 * 

 * @date 2019年2月21日
 */
public interface IDeviceParametersFactory extends BaseFactory<DeviceParametersForm, DeviceParametersVo> {

	@Override
	Pagination<DeviceParametersVo> page(DeviceParametersForm deviceParametersForm);

	@Override
	DeviceParametersVo get(DeviceParametersForm deviceParametersForm);
	
	DeviceParametersVo getParams(DeviceParametersForm deviceParametersForm);

	@Override
	DeviceParametersVo add(DeviceParametersForm deviceParametersForm);

	boolean checkDeviceName(DeviceParametersForm deviceParametersForm);
	
	@Override
	List<DeviceParametersVo> list(DeviceParametersForm deviceParametersForm);

	@Override
	String del(DeviceParametersForm deviceParametersForm);

}
