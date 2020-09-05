package com.dotop.smartwater.project.third.module.service;

import com.dotop.smartwater.project.third.module.api.dao.IWaterCommandDao;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.third.module.api.service.IWaterCommandService;
import com.dotop.smartwater.project.third.module.core.water.bo.CommandBo;
import com.dotop.smartwater.project.third.module.core.water.dto.CommandDto;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class WaterCommandServiceImpl implements IWaterCommandService {

    @Autowired
    IWaterCommandDao iWaterCommandDao;

    @Override
    public CommandVo get(CommandBo commandBo) throws FrameworkRuntimeException {
        CommandDto commandDto = BeanUtils.copy(commandBo, CommandDto.class);
        commandDto.setIsDel(RootModel.NOT_DEL);
        return iWaterCommandDao.get(commandDto);
    }
}
