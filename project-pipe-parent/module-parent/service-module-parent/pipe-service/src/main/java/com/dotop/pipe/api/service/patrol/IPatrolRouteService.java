package com.dotop.pipe.api.service.patrol;


import com.dotop.pipe.core.bo.patrol.PatrolRouteBo;
import com.dotop.pipe.core.vo.patrol.PatrolRouteVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

/**
 * 爆管管理
 *
 */
public interface IPatrolRouteService extends BaseService<PatrolRouteBo, PatrolRouteVo> {


    @Override
    PatrolRouteVo add(PatrolRouteBo patrolRouteBo) throws FrameworkRuntimeException;

    @Override
    PatrolRouteVo get(PatrolRouteBo patrolRouteBo) throws FrameworkRuntimeException;

    @Override
    Pagination<PatrolRouteVo> page(PatrolRouteBo patrolRouteBo) throws FrameworkRuntimeException;

    @Override
    List<PatrolRouteVo> list(PatrolRouteBo patrolRouteBo) throws FrameworkRuntimeException;

    @Override
    PatrolRouteVo edit(PatrolRouteBo patrolRouteBo) throws FrameworkRuntimeException;

    @Override
    String del(PatrolRouteBo patrolRouteBo) throws FrameworkRuntimeException;

}