package com.dotop.pipe.web.api.factory.patrol;


import com.dotop.pipe.core.form.PatrolTaskTimerForm;
import com.dotop.pipe.core.vo.patrol.PatrolTaskTimerVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.Date;
import java.util.List;

public interface IPatrolTaskTimerFactory extends BaseFactory<PatrolTaskTimerForm, PatrolTaskTimerVo> {

    @Override
    PatrolTaskTimerVo add(PatrolTaskTimerForm patrolTaskTimerForm) throws FrameworkRuntimeException;

    @Override
    PatrolTaskTimerVo get(PatrolTaskTimerForm patrolTaskTimerForm) throws FrameworkRuntimeException;

    @Override
    Pagination<PatrolTaskTimerVo> page(PatrolTaskTimerForm patrolTaskTimerForm) throws FrameworkRuntimeException;

    @Override
    List<PatrolTaskTimerVo> list(PatrolTaskTimerForm patrolTaskTimerForm) throws FrameworkRuntimeException;

    @Override
    PatrolTaskTimerVo edit(PatrolTaskTimerForm patrolTaskTimerForm) throws FrameworkRuntimeException;

    PatrolTaskTimerVo editWithOutAuth(PatrolTaskTimerForm patrolTaskTimerForm) throws FrameworkRuntimeException;

    @Override
    String del(PatrolTaskTimerForm patrolTaskTimerForm) throws FrameworkRuntimeException;

    PatrolTaskTimerVo editStatus(PatrolTaskTimerForm patrolTaskTimerForm);

    /**
     * 调用此方法执行定任务分配时任务
     *
     * @param patrolTaskTimerForm
     * @throws FrameworkRuntimeException
     */
    void runTimer(PatrolTaskTimerForm patrolTaskTimerForm) throws FrameworkRuntimeException;

    /**
     * 用于事务
     *
     * @param timerVo
     * @param now
     * @throws FrameworkRuntimeException
     */
    void runTimerForTransactional(PatrolTaskTimerVo timerVo, Date now) throws FrameworkRuntimeException;
}
