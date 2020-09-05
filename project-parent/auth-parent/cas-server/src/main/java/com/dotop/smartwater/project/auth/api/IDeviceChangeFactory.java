package com.dotop.smartwater.project.auth.api;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.DeviceChangeForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceChangeVo;

public interface IDeviceChangeFactory extends BaseFactory<DeviceChangeForm, DeviceChangeVo> {

	/**
	 * 获取菜单并分页
	 */
	Pagination<DeviceChangeVo> page(DeviceChangeForm form) throws FrameworkRuntimeException;
	
}
