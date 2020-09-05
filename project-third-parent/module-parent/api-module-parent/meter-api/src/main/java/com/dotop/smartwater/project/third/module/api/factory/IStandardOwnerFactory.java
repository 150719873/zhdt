package com.dotop.smartwater.project.third.module.api.factory;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.third.module.core.third.standard.form.DataForm;
import com.dotop.smartwater.project.third.module.core.third.standard.vo.StandardOwnerVo;

/**
 *
 */
public interface IStandardOwnerFactory extends IWaterObtainFactory<DataForm, StandardOwnerVo> {


    @Override
    Pagination<StandardOwnerVo> pageOwner(DataForm dataForm)throws FrameworkRuntimeException;
}
