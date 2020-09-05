package com.dotop.smartwater.project.third.server.meterread.dao.water;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.dependence.core.common.BaseDto;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import org.springframework.dao.DataAccessException;

public interface ITestDao extends BaseDao<BaseDto, BaseVo> {

    BaseVo get(BaseDto baseDto) throws DataAccessException;

}
