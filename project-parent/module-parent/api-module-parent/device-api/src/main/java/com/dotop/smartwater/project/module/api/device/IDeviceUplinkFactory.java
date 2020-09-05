package com.dotop.smartwater.project.module.api.device;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceUplinkVo;

/**
 * 上行接口

 * @date 2019/2/25.
 */
public interface IDeviceUplinkFactory extends BaseFactory<DeviceUplinkForm, DeviceUplinkVo> {

	/**
	 * 添加
	 * @param deviceUplinkForm 上行对象
	 * @return 上行对象
	 */
	@Override
	DeviceUplinkVo add(DeviceUplinkForm deviceUplinkForm);

	/**
	 * 查询
	 *
	 * @param id id
	 * @param date 日期
	 * @return 上行对象
	 * @throws FrameworkRuntimeException
	 */
	DeviceUplinkVo findLastUplinkWater(Long id, String date);

	/**
	 * 分页查询
	 *
	 * @param deviceUplinkForm 上行对象
	 * @return 上行对象分页
	 * @throws FrameworkRuntimeException
	 */
	Pagination<DeviceUplinkVo> findOriginal(DeviceUplinkForm deviceUplinkForm);

	/**
	 * 分页查询
	 *
	 * @param deviceUplinkForm 上行对象
	 * @return 上行对象分页
	 * @throws FrameworkRuntimeException
	 */
	Pagination<DeviceUplinkVo> findOriginalCrossMonth(DeviceUplinkForm deviceUplinkForm);

	/**
	 * 分页查询
	 *
	 * @param deviceForm 设备对象
	 * @param start 开始
	 * @param end 结束
	 * @return 上行对象分页
	 * @throws FrameworkRuntimeException
	 */
	Pagination<DeviceUplinkVo> findDownLink(DeviceForm deviceForm, String start, String end);
}
