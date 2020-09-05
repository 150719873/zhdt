package com.dotop.pipe.api.service.log;

import com.dotop.pipe.core.bo.log.LogPointMapBo;
import com.dotop.pipe.core.vo.log.LogPointMapVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.List;

public interface ILogPointMapService {

    List<LogPointMapVo> list(LogPointMapBo logPointMapBo) throws FrameworkRuntimeException;

    void adds(List<LogPointMapBo> logPointMapBos) throws FrameworkRuntimeException;

    void dels(LogPointMapBo logPointMapBo) throws FrameworkRuntimeException;
}
