package com.dotop.smartwater.project.module.dao.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DeviceBatchDto;
import com.dotop.smartwater.project.module.core.water.dto.DeviceBatchRelationDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBatchVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceParametersVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;

/**
 * 设备批次管理

 */
public interface IDeviceBatchDao extends BaseDao<DeviceBatchDto, DeviceBatchVo> {

	@Override
	void add(DeviceBatchDto dto);
	
	int checkBatchIsExist(DeviceBatchDto dto);
	
	@Override
	Integer edit(DeviceBatchDto dto);
	
	int saveRelation(DeviceBatchRelationDto dto);
	
	List<DeviceBatchVo> list(DeviceBatchDto dto);
	
	List<DeviceVo> devicePage(DeviceBatchRelationDto dto);
	
	int delete(DeviceBatchDto dto);
	
	int deleteDevice(DeviceBatchRelationDto dto);
	
	int end(DeviceBatchDto dto);
	
	DeviceBatchVo get(DeviceBatchDto dto);
}
