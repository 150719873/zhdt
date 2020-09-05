package com.dotop.smartwater.project.third.module.api.service;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.bo.CommandBo;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;

import java.util.List;

/**
 *
 */
public interface IMeterCommandService extends BaseService<CommandBo, CommandVo> {


    @Override
    CommandVo add(CommandBo commandBo) throws FrameworkRuntimeException;

    @Override
    CommandVo get(CommandBo commandBo) throws FrameworkRuntimeException;

    @Override
    CommandVo edit(CommandBo commandBo) throws FrameworkRuntimeException;

    @Override
    List<CommandVo> list(CommandBo commandBo) throws FrameworkRuntimeException;
}
