package com.dotop.smartwater.project.third.module.api.factory;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.third.nb2.form.DataBackForm;
import com.dotop.smartwater.project.third.module.core.third.nb2.vo.DeviceUplinkVo;

import java.util.List;

public interface INb2DeviceUplinkFactory extends IWaterObtainFactory<DataBackForm, DeviceUplinkVo> {

    @Override
    List<DeviceUplinkVo> listDeviceUplink(DataBackForm dataBackForm) throws FrameworkRuntimeException;
}
