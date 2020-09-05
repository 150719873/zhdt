package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DeviceBatchDto;
import com.dotop.smartwater.project.module.core.water.dto.DeviceParametersDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceParametersVo;

import java.util.List;

/**

 * @date 2019年2月21日
 */
public interface IDeviceParametersDao extends BaseDao<DeviceParametersDto, DeviceParametersVo> {

	@Override
	List<DeviceParametersVo> list(DeviceParametersDto deviceParametersDto);

	List<DeviceParametersVo> noEndList(DeviceBatchDto dto);
	
	@Override
	DeviceParametersVo get(DeviceParametersDto deviceParametersDto);
	
	DeviceParametersVo getParams(DeviceParametersDto deviceParametersDto);

	@Override
	void add(DeviceParametersDto deviceParametersDto);

	@Override
	Integer edit(DeviceParametersDto deviceParametersDto);

	@Override
	Integer del(DeviceParametersDto deviceParametersDto);

	@Override
	Boolean isExist(DeviceParametersDto deviceParametersDto);
	
	int checkDeviceName(DeviceParametersDto deviceParametersDto);
}
