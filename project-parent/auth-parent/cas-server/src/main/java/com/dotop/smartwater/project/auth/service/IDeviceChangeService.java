package com.dotop.smartwater.project.auth.service;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DeviceChangeBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceChangeVo;

public interface IDeviceChangeService extends BaseService<DeviceChangeBo, DeviceChangeVo> {

	/**
	 *分页
	 */
	Pagination<DeviceChangeVo> page(DeviceChangeBo bo) throws FrameworkRuntimeException;
	
}
