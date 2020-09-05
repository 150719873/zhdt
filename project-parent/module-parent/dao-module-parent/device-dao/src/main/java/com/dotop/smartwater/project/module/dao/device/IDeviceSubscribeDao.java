package com.dotop.smartwater.project.module.dao.device;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DeviceSubscribeDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceSubscribeVo;


public interface IDeviceSubscribeDao extends BaseDao<DeviceSubscribeDto, DeviceSubscribeVo> {

	@Override
	DeviceSubscribeVo get(DeviceSubscribeDto dto) ;


	@Override
	void add(DeviceSubscribeDto dto) ;


	@Override
	Integer del(DeviceSubscribeDto dto);

}
