package com.dotop.pipe.api.service.patrol;


import com.dotop.pipe.core.bo.patrol.PatrolRoutePointBo;
import com.dotop.pipe.core.vo.patrol.PatrolRoutePointVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

/**
 *
 */
public interface IPatrolRoutePointService extends BaseService<PatrolRoutePointBo, PatrolRoutePointVo> {


    @Override
    PatrolRoutePointVo add(PatrolRoutePointBo patrolRoutePointBo) throws FrameworkRuntimeException;

    @Override
    PatrolRoutePointVo get(PatrolRoutePointBo patrolRoutePointBo) throws FrameworkRuntimeException;

    @Override
    Pagination<PatrolRoutePointVo> page(PatrolRoutePointBo patrolRoutePointBo) throws FrameworkRuntimeException;

    @Override
    List<PatrolRoutePointVo> list(PatrolRoutePointBo patrolRoutePointBo) throws FrameworkRuntimeException;

    @Override
    PatrolRoutePointVo edit(PatrolRoutePointBo patrolRoutePointBo) throws FrameworkRuntimeException;

    @Override
    String del(PatrolRoutePointBo patrolRoutePointBo) throws FrameworkRuntimeException;

    void batchAdd(List<PatrolRoutePointBo> pointList);

    void batchUpdate(List<PatrolRoutePointBo> pointList);

    void batchDel(List<PatrolRoutePointBo> pointList);
}
