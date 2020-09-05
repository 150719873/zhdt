package com.dotop.smartwater.project.third.module.api.service;


import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.third.zhsw.form.ClientForm;
import com.dotop.smartwater.project.third.module.core.third.zhsw.form.MeterRecordForm;

import java.util.List;

/**
 *
 */
public interface IZhswService {


    void addData(DockingForm dockingForm, DockingForm loginDockingForm, List<ClientForm> list) throws FrameworkRuntimeException;

    void updateData(DockingForm dockingForm, DockingForm loginDockingForm, List<MeterRecordForm> list) throws FrameworkRuntimeException;
}
