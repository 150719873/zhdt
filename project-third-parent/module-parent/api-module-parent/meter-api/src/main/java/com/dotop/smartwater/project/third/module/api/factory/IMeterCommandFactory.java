package com.dotop.smartwater.project.third.module.api.factory;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.form.CommandForm;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;

import java.util.List;

public interface IMeterCommandFactory extends BaseFactory<CommandForm, CommandVo> {

    @Override
    CommandVo add(CommandForm commandForm) throws FrameworkRuntimeException;

    @Override
    CommandVo get(CommandForm commandForm) throws FrameworkRuntimeException;

    @Override
    CommandVo edit(CommandForm commandForm) throws FrameworkRuntimeException;

    @Override
    List<CommandVo> list(CommandForm commandForm) throws FrameworkRuntimeException;
}
