package com.dotop.smartwater.project.third.module.api.dao;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.third.module.core.water.dto.CommandDto;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;
import org.springframework.dao.DataAccessException;

/**
 *
 */
public interface IWaterCommandDao extends BaseDao<CommandDto, CommandVo> {

    @Override
    CommandVo get(CommandDto commandDto) throws DataAccessException;
}
