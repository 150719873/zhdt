package com.dotop.smartwater.project.third.module.api.factory;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.third.module.core.third.standard.form.DataForm;
import com.dotop.smartwater.project.third.module.core.third.standard.vo.StandardDeviceVo;

/**
 *
 */
public interface IStandardDeviceFactory extends IWaterObtainFactory<DataForm, StandardDeviceVo> {

    @Override
    Pagination<StandardDeviceVo> pageDevice(DataForm dataForm) throws FrameworkRuntimeException;

}
