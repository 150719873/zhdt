package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;

import java.util.List;

/**
 * 修改设备信息
 * 

 * @date 2019年2月25日
 */
public interface IInformationFactory extends BaseFactory<DeviceForm, DeviceVo> {

	/**
	 * 修改设备
	 * 
	 * @param deviceForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	@Override
	DeviceVo edit(DeviceForm deviceForm);

	/**
	 * 删除设备信息
	 */
	@Override
	String del(DeviceForm deviceForm);

	/**
	 * 查询上行数据
	 * @param deviceForm 设备对象Form
	 * @return 水表列表
	 */
	List<DeviceVo> getUplinkData(DeviceForm deviceForm);

	/**
	 * 分页查询水表
	 * 
	 * @param deviceForm 设备对象Form
	 * @return 分页查询水表
	 * @throws FrameworkRuntimeException
	 */
	Pagination<DeviceVo> queryDevice(DeviceForm deviceForm);
	
	
	/**
	 * 获取监控数据
	 * 
	 * @param deviceForm 设备对象Form
	 * @return 分页查询水表
	 * @throws FrameworkRuntimeException
	 */
	Pagination<DeviceVo> getMonitor(DeviceForm deviceForm);
	
	/**
	 * 设备管理-水表监控-获取时间间隔中的每一天
	 * @param deviceForm
	 * @return
	 */
	List<String> getDate(DeviceForm deviceForm);
	

}
