package com.dotop.smartwater.project.third.server.meterread.service.water;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

public interface ITestService extends BaseService<BaseBo, BaseVo> {

    @Override
    BaseVo get(BaseBo baseBo) throws FrameworkRuntimeException;

}
