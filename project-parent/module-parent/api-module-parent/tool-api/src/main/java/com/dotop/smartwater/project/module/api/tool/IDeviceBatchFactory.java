package com.dotop.smartwater.project.module.api.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.DeviceBatchForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceBatchRelatioForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBatchVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceParametersVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;

/**
 * 设备批次管理
 * 

 * 
 */
public interface IDeviceBatchFactory extends BaseFactory<DeviceBatchForm, DeviceBatchVo> {
	
	/**
	 * 新增设备批次信息
	 */
	@Override
	DeviceBatchVo add(DeviceBatchForm form);
	
	/**
	 * 修改设备批次信息
	 */
	@Override
	DeviceBatchVo edit(DeviceBatchForm form);
	
	/**
	 * 删除批次信息
	 * @param form
	 * @return
	 */
	boolean delete(DeviceBatchForm form);
	
	
	/**
	 * 删除批次下设备信息
	 * @param form
	 * @return
	 */
	boolean deleteDevice(DeviceBatchRelatioForm form);
	
	/**
	 * 结束批次信息
	 * @param form
	 * @return
	 */
	boolean end(DeviceBatchForm form);
	
	/**
	 * 设备批次分页查询
	 */
	@Override
	Pagination<DeviceBatchVo> page(DeviceBatchForm form);
	
	/**
	 * 列表
	 */
	List<DeviceParametersVo> noEndList(DeviceBatchForm form);
	
	/**
	 * 设备批次-水表信息
	 * @param form
	 * @return
	 */
	Pagination<DeviceVo> detailPage(DeviceBatchRelatioForm form);
	
	/**
	 * 获取设备批次详情
	 */
	@Override
	DeviceBatchVo get(DeviceBatchForm form);
	
}
