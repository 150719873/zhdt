package com.dotop.smartwater.project.third.module.api.factory;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.third.module.core.water.vo.OwnerVo;

import java.util.List;

public interface IMeterOwnerFactory extends BaseFactory<OwnerForm, OwnerVo> {

    @Override
    List<OwnerVo> list(OwnerForm ownerForm) throws FrameworkRuntimeException;

    void adds(List<OwnerForm> ownerForms) throws FrameworkRuntimeException;

    void edits(List<OwnerForm> ownerForms, String enterpriseid) throws FrameworkRuntimeException;

    @Override
    OwnerVo get(OwnerForm ownerForm) throws FrameworkRuntimeException;

    List<OwnerForm> check(List<OwnerForm> ownerForms) throws FrameworkRuntimeException;
}
