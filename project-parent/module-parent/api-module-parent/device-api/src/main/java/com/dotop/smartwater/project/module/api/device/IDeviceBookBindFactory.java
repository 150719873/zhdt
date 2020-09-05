package com.dotop.smartwater.project.module.api.device;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.DeviceBookBindForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBookBindVo;

/**
 * 

 * @description 表册绑定抄表员
 * @date 2019-10-23 08:54
 *
 */
public interface IDeviceBookBindFactory extends BaseFactory<DeviceBookBindForm, DeviceBookBindVo> {

	/**
	 * 表册批量绑定抄表员
	 * @param deviceBookBindForms
	 * @return
	 */
	String configureDeviceBookBind(List<DeviceBookBindForm> deviceBookBindForms);
	/**
	 * 获取表册绑定的抄表员
	 * @param deviceBookBindForm
	 * @return
	 */
	List<DeviceBookBindVo> listDeviceBookBind(DeviceBookBindForm deviceBookBindForm);
	/**
	 * 删除表册绑定的抄表员
	 * @param deviceBookBindForm
	 * @return
	 */
	String deleteDeviceBookBind(DeviceBookBindForm deviceBookBindForm);
	/**
	 * 分页获取表册所绑业主
	 * @param deviceBookBindBo
	 * @return
	 */
	Pagination<DeviceBookBindVo> pageBindOwner(DeviceBookBindForm deviceBookBindForm);
	/**
	 * 获取要绑定的所有业主
	 * @param deviceBookBindBo
	 * @return
	 */
	Pagination<DeviceBookBindVo> listBindOwner(DeviceBookBindForm deviceBookBindForm);
	/**
	 * 表册批量绑定业主
	 * @param deviceBookBindBos
	 * @return
	 */
	String bindOwner(List<DeviceBookBindForm> deviceBookBindForms);
	/**
	 * 表册解绑业主
	 * @param deviceBookBindBo
	 * @return
	 */
	String deleteBindOwner(DeviceBookBindForm deviceBookBindForm);
}
