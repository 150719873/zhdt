package com.dotop.smartwater.project.module.api.device;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.DeviceDownlinkForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;

import java.util.List;

/**
 * 下行接口

 * @date 2019/2/25.
 */
public interface IDeviceDownlinkFactory extends BaseFactory<DeviceDownlinkForm, DeviceDownlinkVo> {
	/**
	 * 添加
	 *
	 * @param deviceDownlinkForm 下行参数对象
	 * @return
	 * @
	 */
	@Override
	DeviceDownlinkVo add(DeviceDownlinkForm deviceDownlinkForm) ;

	/**
	 * 根据clientid查询
	 *
	 * @param deviceDownlinkForm 下行参数对象
	 * @return 下行列表
	 */
	List<DeviceDownlinkVo> findByClientId(DeviceDownlinkForm deviceDownlinkForm) ;

	/**
	 * 更新
	 *
	 * @param deviceDownlinkForm 下行参数对象
	 */
	void update(DeviceDownlinkForm deviceDownlinkForm) ;

	/**
	 * @param deviceDownlinkForm 下行参数对象
	 * @return
	 */
	@Override
	DeviceDownlinkVo get(DeviceDownlinkForm deviceDownlinkForm);

	/**
	 * @param deviceDownlinkForm  下行参数对象
	 * @return 
	 */
	@Override
	Pagination<DeviceDownlinkVo> page(DeviceDownlinkForm deviceDownlinkForm);

	DeviceDownlinkVo getLastDownLink(String devid);
}
