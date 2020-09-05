package com.dotop.smartwater.project.module.dao.device;


import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DeviceChangeDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceChangeVo;

/**
 * APP-更换水表

 *
 */
public interface IDeviceChangeDao extends BaseDao<DeviceChangeDto, DeviceChangeVo> {
	
	/**
	 * 插入换表记录
	 * @param dto
	 * @return
	 */
	int insertDeviceChangeRecord(DeviceChangeDto dto);
	
	/**
	 * 查询换表记录
	 * @param dto
	 * @return
	 */
	List<DeviceChangeVo> getList(DeviceChangeDto dto);
	
}
