package com.dotop.smartwater.project.module.service.device;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBookBindBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBookBindVo;

/**
 * 

 * @description 表册绑定抄表员
 * @date 2019-10-23 08:54
 *
 */
public interface IDeviceBookBindService extends BaseService<DeviceBookBindBo, DeviceBookBindVo> {
	/**
	 * 表册批量绑定抄表员
	 * @param deviceBookBindForms
	 * @return
	 */
	String configureDeviceBookBind(List<DeviceBookBindBo> deviceBookBindBos);
	/**
	 * 获取表册绑定的抄表员
	 * @param deviceBookBindForm
	 * @return
	 */
	List<DeviceBookBindVo> listDeviceBookBind(DeviceBookBindBo deviceBookBindBo);
	/**
	 * 删除表册绑定的抄表员
	 * @param deviceBookBindForm
	 * @return
	 */
	String deleteDeviceBookBind(DeviceBookBindBo deviceBookBindBo);
	/**
	 * 分页获取表册所绑业主
	 * @param deviceBookBindBo
	 * @return
	 */
	Pagination<DeviceBookBindVo> pageBindOwner(DeviceBookBindBo deviceBookBindBo);
	/**
	 * 获取要绑定的所有业主
	 * @param deviceBookBindBo
	 * @return
	 */
	Pagination<DeviceBookBindVo> listBindOwner(DeviceBookBindBo deviceBookBindBo);
	/**
	 * 表册批量绑定业主
	 * @param deviceBookBindBos
	 * @return
	 */
	String bindOwner(List<DeviceBookBindBo> deviceBookBindBos);
	/**
	 * 表册解绑业主
	 * @param deviceBookBindBo
	 * @return
	 */
	String deleteBindOwner(DeviceBookBindBo deviceBookBindBo);
}
