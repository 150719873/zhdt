package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.third.module.api.factory.IMeterCommandFactory;
import com.dotop.smartwater.project.third.module.api.service.IMeterCommandService;
import com.dotop.smartwater.project.third.module.core.water.bo.CommandBo;
import com.dotop.smartwater.project.third.module.core.water.form.CommandForm;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Component
public class MeterCommandFactoryImpl implements IMeterCommandFactory {

    @Autowired
    private IMeterCommandService iMeterCommandService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public CommandVo add(CommandForm commandForm) throws FrameworkRuntimeException {
        CommandBo commandBo = BeanUtils.copy(commandForm, CommandBo.class);
        if (StringUtils.isBlank(commandBo.getId())) {
            commandBo.setId(UuidUtils.getUuid());
        }
        return iMeterCommandService.add(commandBo);
    }

    @Override
    public CommandVo get(CommandForm commandForm) throws FrameworkRuntimeException {
        CommandBo commandBo = BeanUtils.copy(commandForm, CommandBo.class);
        return iMeterCommandService.get(commandBo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public CommandVo edit(CommandForm commandForm) throws FrameworkRuntimeException {
        CommandBo commandBo = BeanUtils.copy(commandForm, CommandBo.class);
        return iMeterCommandService.edit(commandBo);
    }

    @Override
    public List<CommandVo> list(CommandForm commandForm) throws FrameworkRuntimeException {
        CommandBo commandBo = BeanUtils.copy(commandForm, CommandBo.class);
        return iMeterCommandService.list(commandBo);
    }
}
