package com.dotop.pipe.api.dao.runingdata;

import com.dotop.pipe.core.dto.runingdata.RuningDataDto;
import com.dotop.pipe.core.vo.runingdata.RuningDataVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface IRuningDataDao extends BaseDao<RuningDataDto, RuningDataVo> {

    void add(RuningDataDto runingDataDto) throws DataAccessException;

    @Override
    List<RuningDataVo> list(RuningDataDto runingDataDto) throws DataAccessException;

    void makeData(RuningDataDto runingDataDto) throws DataAccessException;

    @Override
    Integer del(RuningDataDto runingDataDto) throws DataAccessException;

    @Override
    Integer edit(RuningDataDto runingDataDto) throws DataAccessException;
    List<RuningDataVo> getRuningTask(RuningDataDto runingDataDto) throws DataAccessException;

}
