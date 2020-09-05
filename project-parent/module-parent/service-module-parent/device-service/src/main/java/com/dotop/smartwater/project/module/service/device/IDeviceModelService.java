package com.dotop.smartwater.project.module.service.device;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DeviceModelBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceModelVo;

/**

 * @date 2019/2/26.
 */
public interface IDeviceModelService extends BaseService<DeviceModelBo, DeviceModelVo> {


	/**
	 * 查询设备信息
	 *
	 * @param deviceModelBo
	 * @return
	 * @
	 */
	Pagination<DeviceModelVo> find(DeviceModelBo deviceModelBo) ;

	/**
	 * 查询设备信息-不分页
	 *
	 * @param deviceModelBo
	 * @return
	 * @
	 */
	List<DeviceModelVo> noPagingfind(DeviceModelBo deviceModelBo) ;

	/**
	 * 新增设备类型
	 *
	 * @param deviceModelBo
	 * @
	 */
	void save(DeviceModelBo deviceModelBo) ;

	/**
	 * 修改设备类型
	 *
	 * @param deviceModelBo
	 * @
	 */
	void update(DeviceModelBo deviceModelBo) ;

	/**
	 * 删除设备类型
	 *
	 * @param deviceModelBo
	 * @
	 */
	void delete(DeviceModelBo deviceModelBo) ;

	/**
	 * 根据ID查询设备型号
	 *
	 * @param deviceModelBo
	 * @return
	 * @
	 */
	@Override
	DeviceModelVo get(DeviceModelBo deviceModelBo) ;

}
