package com.dotop.smartwater.project.module.api.mode;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.water.form.customize.DownlinkForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;

import java.util.Map;

/**
 * 各种通讯方式接口实现
 *

 * @date 2019/10/14.
 */
public interface IModeFactory extends BaseFactory<BaseForm, BaseVo> {

    /**
     * 东信协议的设备下行入口
     *
     * @return
     */
    Map<String, String> dxDeviceTx(DeviceVo deviceVo, Integer command, String mode, DownlinkForm params);

    /**
     * 华奥通协议的设备下行入口
     *
     * @return
     */
    Map<String,String> hatDeviceTx(DeviceVo deviceVo, Integer command, String mode, String value);
}
