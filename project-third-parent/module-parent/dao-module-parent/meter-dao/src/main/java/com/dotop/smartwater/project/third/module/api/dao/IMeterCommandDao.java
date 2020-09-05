package com.dotop.smartwater.project.third.module.api.dao;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.dto.CommandDto;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 *
 */
public interface IMeterCommandDao extends BaseDao<CommandDto, CommandVo> {

    @Override
    void add(CommandDto commandDto) throws FrameworkRuntimeException;

    @Override
    CommandVo get(CommandDto commandDto) throws FrameworkRuntimeException;

    @Override
    Integer edit(CommandDto commandDto) throws FrameworkRuntimeException;

    @Override
    List<CommandVo> list(CommandDto commandDto) throws DataAccessException;
}
