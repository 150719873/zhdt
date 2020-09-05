package com.dotop.pipe.web.api.factory.log;

import com.dotop.pipe.core.form.LogMainForm;
import com.dotop.pipe.core.vo.log.LogMainVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface ILogMainFactory extends BaseFactory<LogMainForm, LogMainVo> {

    @Override
    Pagination<LogMainVo> page(LogMainForm logMainForm) throws FrameworkRuntimeException;

    @Override
    LogMainVo add(LogMainForm logMainForm) throws FrameworkRuntimeException;

    @Override
    LogMainVo get(LogMainForm logMainForm) throws FrameworkRuntimeException;

    @Override
    LogMainVo edit(LogMainForm logMainForm) throws FrameworkRuntimeException;

    @Override
    String del(LogMainForm logMainForm) throws FrameworkRuntimeException;

    Integer getMaxVersion(LogMainForm logMainForm) throws FrameworkRuntimeException;
}
