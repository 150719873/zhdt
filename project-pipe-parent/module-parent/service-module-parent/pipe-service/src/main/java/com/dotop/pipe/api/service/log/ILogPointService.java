package com.dotop.pipe.api.service.log;

import com.dotop.pipe.core.bo.log.LogPointBo;
import com.dotop.pipe.core.vo.log.LogPointVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.List;

public interface ILogPointService {

    List<LogPointVo> list(LogPointBo logPointBo) throws FrameworkRuntimeException;

    void adds(List<LogPointBo> logPointBos) throws FrameworkRuntimeException;

    void dels(LogPointBo logPointBo) throws FrameworkRuntimeException;
}
