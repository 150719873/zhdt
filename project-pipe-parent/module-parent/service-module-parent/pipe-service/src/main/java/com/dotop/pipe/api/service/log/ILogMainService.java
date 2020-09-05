package com.dotop.pipe.api.service.log;

import com.dotop.pipe.core.bo.log.LogMainBo;
import com.dotop.pipe.core.vo.log.LogMainVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;


public interface ILogMainService extends BaseService<LogMainBo, LogMainVo> {

    @Override
    Pagination<LogMainVo> page(LogMainBo logMainBo) throws FrameworkRuntimeException;

    LogMainVo add(LogMainBo logMainBo) throws FrameworkRuntimeException;

    @Override
    LogMainVo edit(LogMainBo logMainBo) throws FrameworkRuntimeException;

    @Override
    LogMainVo get(LogMainBo logMainBo) throws FrameworkRuntimeException;

    @Override
    String del(LogMainBo logMainBo) throws FrameworkRuntimeException;

    Integer getMaxVersion(LogMainBo logMainBo) throws FrameworkRuntimeException;
}
