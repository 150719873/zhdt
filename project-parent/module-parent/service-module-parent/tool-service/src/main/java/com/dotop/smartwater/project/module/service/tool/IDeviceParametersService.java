package com.dotop.smartwater.project.module.service.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBatchBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceParametersBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceParametersVo;

/**
 * 

 * @date 2019年2月21日
 */
public interface IDeviceParametersService extends BaseService<DeviceParametersBo, DeviceParametersVo> {

	@Override
	Pagination<DeviceParametersVo> page(DeviceParametersBo deviceParametersBo);

	@Override
	DeviceParametersVo get(DeviceParametersBo deviceParametersBo);
	
	
	DeviceParametersVo getParams(DeviceParametersBo deviceParametersBo);

	@Override
	DeviceParametersVo add(DeviceParametersBo deviceParametersBo);

	@Override
	List<DeviceParametersVo> list(DeviceParametersBo deviceParametersBo);
	
	List<DeviceParametersVo> noEndList(DeviceBatchBo bo);

	@Override
	String del(DeviceParametersBo deviceParametersBo);

	@Override
	boolean isExist(DeviceParametersBo deviceParametersBo);
	
	boolean checkDeviceName(DeviceParametersBo deviceParametersBo);
}
