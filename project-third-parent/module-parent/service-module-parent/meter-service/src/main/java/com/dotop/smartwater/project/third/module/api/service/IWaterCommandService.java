package com.dotop.smartwater.project.third.module.api.service;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.bo.CommandBo;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;

/**
 *
 */
public interface IWaterCommandService extends BaseService<CommandBo, CommandVo> {

    @Override
    CommandVo get(CommandBo commandBo) throws FrameworkRuntimeException;
}
