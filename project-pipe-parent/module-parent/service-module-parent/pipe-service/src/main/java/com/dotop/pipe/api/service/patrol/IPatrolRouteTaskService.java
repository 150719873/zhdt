package com.dotop.pipe.api.service.patrol;


import com.dotop.pipe.core.bo.patrol.PatrolRouteTaskBo;
import com.dotop.pipe.core.vo.patrol.PatrolRouteTaskVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

/**
 * 爆管管理
 *
 */
public interface IPatrolRouteTaskService extends BaseService<PatrolRouteTaskBo, PatrolRouteTaskVo> {


    @Override
    PatrolRouteTaskVo add(PatrolRouteTaskBo patrolRouteBo) throws FrameworkRuntimeException;

    @Override
    PatrolRouteTaskVo get(PatrolRouteTaskBo patrolRouteBo) throws FrameworkRuntimeException;

    @Override
    Pagination<PatrolRouteTaskVo> page(PatrolRouteTaskBo patrolRouteBo) throws FrameworkRuntimeException;

    @Override
    List<PatrolRouteTaskVo> list(PatrolRouteTaskBo patrolRouteBo) throws FrameworkRuntimeException;

    @Override
    PatrolRouteTaskVo edit(PatrolRouteTaskBo patrolRouteBo) throws FrameworkRuntimeException;

    @Override
    String del(PatrolRouteTaskBo patrolRouteBo) throws FrameworkRuntimeException;

    PatrolRouteTaskVo editStatus(PatrolRouteTaskBo patrolRouteBo);


    void batchAdd(List<PatrolRouteTaskBo> taskList);

    void batchUpdate(List<PatrolRouteTaskBo> taskList);
}