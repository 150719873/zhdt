package com.dotop.smartwater.project.third.module.api.factory;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceUplinkVo;

import java.util.List;

public interface IMeterDeviceUplinkFactory extends BaseFactory<DeviceUplinkForm, DeviceUplinkVo> {


    void adds(List<DeviceUplinkForm> deviceUplinkForms) throws FrameworkRuntimeException;

    @Override
    List<DeviceUplinkVo> list(DeviceUplinkForm deviceUplinkForm) throws FrameworkRuntimeException;


    void edits(List<DeviceUplinkForm> deviceUplinkForms) throws FrameworkRuntimeException;

    List<DeviceUplinkForm> check(List<DeviceUplinkForm> deviceUplinkForms) throws FrameworkRuntimeException;
}
