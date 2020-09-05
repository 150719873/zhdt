package com.dotop.smartwater.project.third.module.api.factory;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.vo.DockingVo;

import java.util.List;

/**
 *
 */
public interface IMeterDockingFactory extends BaseFactory<DockingForm, DockingVo> {

    @Override
    List<DockingVo> list(DockingForm dockingForm) throws FrameworkRuntimeException;

    @Override
    DockingVo get(DockingForm dockingForm) throws FrameworkRuntimeException;

    boolean isRealUser(DockingForm dockingForm) throws FrameworkRuntimeException;
}
