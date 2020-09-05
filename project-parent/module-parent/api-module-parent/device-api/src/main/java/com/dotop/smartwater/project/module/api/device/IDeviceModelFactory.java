package com.dotop.smartwater.project.module.api.device;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.DeviceModelForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceModelVo;

import java.util.List;

/**
 * 设备模型

 * @date 2019/2/26.
 */
public interface IDeviceModelFactory extends BaseFactory<DeviceModelForm, DeviceModelVo> {

	/**
	 * 查询设备信息
	 *
	 * @param deviceModelForm 模型对象
	 * @return
	 * @
	 */
	Pagination<DeviceModelVo> find(DeviceModelForm deviceModelForm) ;

	/**
	 * 查询设备信息-不分页
	 * @param deviceModelForm 模型对象
	 * @return 列表
	 * @
	 */
	List<DeviceModelVo> noPagingfind(DeviceModelForm deviceModelForm) ;

	/**
	 * 新增设备类型
	 *
	 * @param deviceModelForm
	 * @
	 */
	void save(DeviceModelForm deviceModelForm) ;

	/**
	 * 修改设备类型
	 *
	 * @param deviceModelForm
	 * @
	 */
	void update(DeviceModelForm deviceModelForm) ;

	/**
	 * 删除设备类型
	 *
	 * @param deviceModelForm
	 * @
	 */
	void delete(DeviceModelForm deviceModelForm) ;

	/**
	 * 根据ID查询设备型号
	 *
	 * @param deviceModelForm
	 * @return
	 * @
	 */
	@Override
	DeviceModelVo get(DeviceModelForm deviceModelForm) ;
}
