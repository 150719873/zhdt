package com.dotop.smartwater.project.module.dao.device;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DeviceModelDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceModelVo;

import java.util.List;

/**

 * @date 2019/2/26.
 */
public interface IDeviceModelDao extends BaseDao<DeviceModelDto, DeviceModelVo> {

	/**
	 * 查询列表
	 *
	 * @param deviceModelDto
	 * @return
	 * @
	 */
	List<DeviceModelVo> getList(DeviceModelDto deviceModelDto);

	/**
	 * 新增
	 *
	 * @param deviceModelDto
	 * @
	 */
	void save(DeviceModelDto deviceModelDto);

	/**
	 * 更新
	 *
	 * @param deviceModelDto
	 * @
	 */
	void update(DeviceModelDto deviceModelDto);

	/**
	 * 删除
	 *
	 * @param deviceModelDto
	 * @
	 */
	void delete(DeviceModelDto deviceModelDto);

	/**
	 * 根据id查询
	 *
	 * @param deviceModelDto
	 * @return
	 * @
	 */
	@Override
	DeviceModelVo get(DeviceModelDto deviceModelDto);
}
