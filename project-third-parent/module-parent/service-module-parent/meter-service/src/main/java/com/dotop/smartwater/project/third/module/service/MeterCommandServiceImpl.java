package com.dotop.smartwater.project.third.module.service;

import com.dotop.smartwater.project.third.module.api.dao.IMeterCommandDao;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.third.module.api.service.IMeterCommandService;
import com.dotop.smartwater.project.third.module.core.water.bo.CommandBo;
import com.dotop.smartwater.project.third.module.core.water.dto.CommandDto;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
public class MeterCommandServiceImpl implements IMeterCommandService {

    @Autowired
    private IMeterCommandDao iMeterCommandDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public CommandVo add(CommandBo commandBo) throws FrameworkRuntimeException {
        CommandDto commandDto = BeanUtils.copy(commandBo, CommandDto.class);
        commandDto.setIsDel(RootModel.NOT_DEL);
        commandDto.setCurr(new Date());
        iMeterCommandDao.add(commandDto);
        return BeanUtils.copy(commandBo, CommandVo.class);
    }

    @Override
    public CommandVo get(CommandBo commandBo) throws FrameworkRuntimeException {
        CommandDto commandDto = BeanUtils.copy(commandBo, CommandDto.class);
        commandDto.setIsDel(RootModel.NOT_DEL);
        return iMeterCommandDao.get(commandDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public CommandVo edit(CommandBo commandBo) throws FrameworkRuntimeException {
        CommandDto commandDto = BeanUtils.copy(commandBo, CommandDto.class);
        commandDto.setIsDel(RootModel.NOT_DEL);
        commandDto.setCurr(new Date());
        if (iMeterCommandDao.edit(commandDto) != 1) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION);
        }
        return BeanUtils.copy(commandBo, CommandVo.class);
    }

    @Override
    public List<CommandVo> list(CommandBo commandBo) throws FrameworkRuntimeException {
        CommandDto commandDto = BeanUtils.copy(commandBo, CommandDto.class);
        commandDto.setIsDel(RootModel.NOT_DEL);
        return iMeterCommandDao.list(commandDto);
    }
}
