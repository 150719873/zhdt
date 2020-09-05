package com.dotop.smartwater.project.third.module.api.factory.kbl;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.api.factory.IThirdObtainFactory;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;

/**
 *
 */
public interface IKblDeviceFactory extends IThirdObtainFactory {

    @Override
    void updateDevice(DockingForm loginForm, DeviceForm deviceForm) throws FrameworkRuntimeException;
}
