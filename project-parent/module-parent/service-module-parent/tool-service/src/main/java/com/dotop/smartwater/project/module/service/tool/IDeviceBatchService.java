package com.dotop.smartwater.project.module.service.tool;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBatchBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBatchRelationBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBatchVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;

/**
 * 设备批次管理

 */
public interface IDeviceBatchService extends BaseService<DeviceBatchBo, DeviceBatchVo> {

	@Override
	DeviceBatchVo add(DeviceBatchBo bo);
	
	boolean checkBatchIsExist(DeviceBatchBo bo);
	
	@Override
	DeviceBatchVo edit(DeviceBatchBo bo);
	
	void updateBatch(DeviceBatchRelationBo bo);
	
	@Override
	Pagination<DeviceBatchVo> page(DeviceBatchBo bo);
	
	Pagination<DeviceVo> detailPage(DeviceBatchRelationBo bo);
	
	boolean delete(DeviceBatchBo bo);
	
	boolean deleteDevice(DeviceBatchRelationBo bo);
	
	boolean end(DeviceBatchBo bo);

	@Override
	DeviceBatchVo get(DeviceBatchBo bo);
	
	
}
