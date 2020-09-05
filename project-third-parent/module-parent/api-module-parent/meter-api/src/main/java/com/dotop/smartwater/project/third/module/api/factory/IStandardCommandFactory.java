package com.dotop.smartwater.project.third.module.api.factory;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.water.form.customize.OperationForm;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;

public interface IStandardCommandFactory {

    CommandVo sendCommand(OperationForm operationForm) throws FrameworkRuntimeException;
}
