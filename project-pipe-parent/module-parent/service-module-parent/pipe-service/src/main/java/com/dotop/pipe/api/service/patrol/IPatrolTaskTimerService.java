package com.dotop.pipe.api.service.patrol;


import com.dotop.pipe.core.bo.patrol.PatrolTaskTimerBo;
import com.dotop.pipe.core.vo.patrol.PatrolTaskTimerVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

/**
 * 爆管管理
 */
public interface IPatrolTaskTimerService extends BaseService<PatrolTaskTimerBo, PatrolTaskTimerVo> {


    @Override
    PatrolTaskTimerVo add(PatrolTaskTimerBo patrolTaskTimerBo) throws FrameworkRuntimeException;

    @Override
    PatrolTaskTimerVo get(PatrolTaskTimerBo patrolTaskTimerBo) throws FrameworkRuntimeException;

    @Override
    Pagination<PatrolTaskTimerVo> page(PatrolTaskTimerBo patrolTaskTimerBo) throws FrameworkRuntimeException;

    @Override
    List<PatrolTaskTimerVo> list(PatrolTaskTimerBo patrolTaskTimerBo) throws FrameworkRuntimeException;

    @Override
    PatrolTaskTimerVo edit(PatrolTaskTimerBo patrolTaskTimerBo) throws FrameworkRuntimeException;

    @Override
    String del(PatrolTaskTimerBo patrolTaskTimerBo) throws FrameworkRuntimeException;

    PatrolTaskTimerVo editStatus(PatrolTaskTimerBo patrolTaskTimerBo);
}
