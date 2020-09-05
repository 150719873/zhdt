package com.dotop.pipe.api.dao.log;

import com.dotop.pipe.core.dto.log.LogPointDto;
import com.dotop.pipe.core.vo.log.LogPointVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ILogPointDao extends BaseDao<LogPointDto, LogPointVo> {

    @Override
    List<LogPointVo> list(LogPointDto logPointDto) throws DataAccessException;

    @Override
    void adds(@Param("logPointDtos") List<LogPointDto> logPointDtos) throws DataAccessException;

    @Override
    Integer edit(LogPointDto logPointDto) throws DataAccessException;

    @Override
    Integer del(LogPointDto logPointDto) throws DataAccessException;
}
