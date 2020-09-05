package com.dotop.pipe.api.service.area;

import java.util.Date;

import com.dotop.pipe.core.vo.area.AreaMapVo;
import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

@Deprecated
// 区域与设备关系
public interface IAreaMapService extends BaseService<BaseBo, AreaMapVo> {

	// 新增区域与设备关系
	public void add(String enterpriseId, String areaId, String deviceId, Date curr, String userBy)
			throws FrameworkRuntimeException;

	// 删除区域与设备关系
	public void del(String enterpriseId, String deviceId, Date curr, String userBy) throws FrameworkRuntimeException;

	// 是否存在区域关系(用于判断区域是否可以被删除)
	public Boolean isExist(String enterpriseId, String areaId) throws FrameworkRuntimeException;

	// 设备是否存在区域关系
	public Boolean isExist(String enterpriseId, String areaId, String deviceId) throws FrameworkRuntimeException;
}
