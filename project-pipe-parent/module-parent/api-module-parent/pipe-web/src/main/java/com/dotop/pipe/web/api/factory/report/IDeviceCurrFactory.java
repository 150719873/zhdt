package com.dotop.pipe.web.api.factory.report;

import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.vo.report.DeviceCurrVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface IDeviceCurrFactory extends BaseFactory<DeviceForm, DeviceCurrVo> {

    @Override
    Pagination<DeviceCurrVo> page(DeviceForm deviceForm) throws FrameworkRuntimeException;
}
