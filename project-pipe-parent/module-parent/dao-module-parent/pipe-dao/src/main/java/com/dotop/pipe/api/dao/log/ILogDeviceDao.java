package com.dotop.pipe.api.dao.log;

import com.dotop.pipe.core.dto.log.LogDeviceDto;
import com.dotop.pipe.core.vo.log.LogDeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ILogDeviceDao extends BaseDao<LogDeviceDto, LogDeviceVo> {

    @Override
    List<LogDeviceVo> list(LogDeviceDto logDeviceDto) throws DataAccessException;

    @Override
    void adds(@Param("logDeviceDtos") List<LogDeviceDto> logDeviceDtos) throws DataAccessException;

    @Override
    Integer edit(LogDeviceDto logDeviceDto) throws DataAccessException;

    @Override
    Integer del(LogDeviceDto logDeviceDto) throws DataAccessException;
}
