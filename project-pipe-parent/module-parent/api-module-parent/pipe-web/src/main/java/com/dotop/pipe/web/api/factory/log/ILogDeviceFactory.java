package com.dotop.pipe.web.api.factory.log;

import com.dotop.pipe.core.form.LogDeviceForm;
import com.dotop.pipe.core.vo.log.LogDeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.List;

public interface ILogDeviceFactory extends BaseFactory<LogDeviceForm, LogDeviceVo> {

    @Override
    List<LogDeviceVo> list(LogDeviceForm logDeviceForm) throws FrameworkRuntimeException;
}
