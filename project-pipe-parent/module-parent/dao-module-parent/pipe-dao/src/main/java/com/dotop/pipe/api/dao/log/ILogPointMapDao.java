package com.dotop.pipe.api.dao.log;

import com.dotop.pipe.core.dto.log.LogPointMapDto;
import com.dotop.pipe.core.vo.log.LogPointMapVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ILogPointMapDao extends BaseDao<LogPointMapDto, LogPointMapVo> {

    @Override
    List<LogPointMapVo> list(@Param("logPointMapDto") LogPointMapDto logPointMapDto) throws DataAccessException;

    @Override
    void adds(@Param("logPointMapDtos") List<LogPointMapDto> logPointMapDtos) throws DataAccessException;

    @Autowired
    Integer edit(LogPointMapDto logPointMapDto) throws DataAccessException;

    @Autowired
    Integer del(LogPointMapDto logPointMapDto) throws DataAccessException;
}
