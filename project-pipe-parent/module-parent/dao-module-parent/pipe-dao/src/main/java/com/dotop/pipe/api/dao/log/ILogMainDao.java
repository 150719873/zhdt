package com.dotop.pipe.api.dao.log;

import com.dotop.pipe.core.dto.log.LogMainDto;
import com.dotop.pipe.core.vo.log.LogMainVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ILogMainDao extends BaseDao<LogMainDto, LogMainVo> {

    @Override
    List<LogMainVo> list(LogMainDto logMainDto) throws DataAccessException;

    @Override
    void add(LogMainDto logMainDto) throws DataAccessException;

    @Override
    Integer edit(LogMainDto logMainDto) throws DataAccessException;

    @Override
    Integer del(LogMainDto logMainDto) throws DataAccessException;

    @Override
    LogMainVo get(LogMainDto logMainDto) throws DataAccessException;

    Integer getMaxVersion(LogMainDto logMainDto) throws DataAccessException;
}
