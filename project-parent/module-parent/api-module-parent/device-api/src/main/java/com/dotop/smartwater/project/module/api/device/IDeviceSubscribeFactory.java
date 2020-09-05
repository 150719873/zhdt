package com.dotop.smartwater.project.module.api.device;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.water.form.DeviceSubscribeForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceSubscribeVo;


/**
 *

 * @date 2019年3月4日
 */
public interface IDeviceSubscribeFactory extends BaseFactory<DeviceSubscribeForm, DeviceSubscribeVo> {


    @Override
    DeviceSubscribeVo add(DeviceSubscribeForm form) ;


    @Override
    String del(DeviceSubscribeForm form) ;

}
