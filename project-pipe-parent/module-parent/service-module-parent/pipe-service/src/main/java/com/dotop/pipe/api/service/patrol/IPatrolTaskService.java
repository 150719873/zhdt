package com.dotop.pipe.api.service.patrol;


import com.dotop.pipe.core.bo.patrol.PatrolTaskBo;
import com.dotop.pipe.core.vo.patrol.PatrolTaskVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

/**
 * 爆管管理
 *
 */
public interface IPatrolTaskService extends BaseService<PatrolTaskBo, PatrolTaskVo> {


    @Override
    PatrolTaskVo add(PatrolTaskBo patrolRouteBo) throws FrameworkRuntimeException;

    @Override
    PatrolTaskVo get(PatrolTaskBo patrolRouteBo) throws FrameworkRuntimeException;

    @Override
    Pagination<PatrolTaskVo> page(PatrolTaskBo patrolRouteBo) throws FrameworkRuntimeException;

    @Override
    List<PatrolTaskVo> list(PatrolTaskBo patrolRouteBo) throws FrameworkRuntimeException;

    @Override
    PatrolTaskVo edit(PatrolTaskBo patrolRouteBo) throws FrameworkRuntimeException;

    @Override
    String del(PatrolTaskBo patrolRouteBo) throws FrameworkRuntimeException;

    PatrolTaskVo editStatus(PatrolTaskBo patrolRouteBo);


    void batchUpdateStatus(List<PatrolTaskBo> batchList, String enterpriseId, String status);
}