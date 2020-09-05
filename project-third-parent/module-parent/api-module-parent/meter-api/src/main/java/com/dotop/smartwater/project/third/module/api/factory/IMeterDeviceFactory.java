package com.dotop.smartwater.project.third.module.api.factory;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;

import java.util.List;

public interface IMeterDeviceFactory extends BaseFactory<DeviceForm, DeviceVo> {


    @Override
    List<DeviceVo> list(DeviceForm deviceForm) throws FrameworkRuntimeException;

    void adds(List<DeviceForm> deviceForms) throws FrameworkRuntimeException;

    void edits(List<DeviceForm> deviceForms) throws FrameworkRuntimeException;

    @Override
    DeviceVo get(DeviceForm deviceForm) throws FrameworkRuntimeException;

    List<DeviceForm> check(List<DeviceForm> deviceForms, DockingForm loginDocking) throws FrameworkRuntimeException;
}
