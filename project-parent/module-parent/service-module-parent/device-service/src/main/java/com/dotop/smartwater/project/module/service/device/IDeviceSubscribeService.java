package com.dotop.smartwater.project.module.service.device;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.DeviceSubscribeBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceSubscribeVo;

/**

 * @date 2019/2/25.
 */
public interface IDeviceSubscribeService extends BaseService<DeviceSubscribeBo, DeviceSubscribeVo> {


	@Override
	DeviceSubscribeVo get(DeviceSubscribeBo bo) ;


	@Override
	DeviceSubscribeVo add(DeviceSubscribeBo bo) ;


	@Override
	String del(DeviceSubscribeBo bo);
}
