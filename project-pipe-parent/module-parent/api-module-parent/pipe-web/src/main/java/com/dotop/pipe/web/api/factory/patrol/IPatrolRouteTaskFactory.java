package com.dotop.pipe.web.api.factory.patrol;


import com.dotop.pipe.core.form.PatrolRouteTaskForm;
import com.dotop.pipe.core.vo.patrol.PatrolRouteTaskVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

public interface IPatrolRouteTaskFactory extends BaseFactory<PatrolRouteTaskForm, PatrolRouteTaskVo> {

    @Override
    PatrolRouteTaskVo add(PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException;

    @Override
    PatrolRouteTaskVo get(PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException;

    @Override
    Pagination<PatrolRouteTaskVo> page(PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException;

    @Override
    List<PatrolRouteTaskVo> list(PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException;

    @Override
    PatrolRouteTaskVo edit(PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException;

    @Override
    String del(PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException;

    PatrolRouteTaskVo editStatus(PatrolRouteTaskForm patrolRouteTaskForm);


    void batchAdd(List<PatrolRouteTaskForm> tasks);

    void batUpdate(List<PatrolRouteTaskForm> tasks);

    void batDel(List<PatrolRouteTaskForm> tasks);

    /**
     * 批量新增，无需登录
     *
     * @param tasks
     */
    void batchAddWithOutAuth(List<PatrolRouteTaskForm> tasks);
}
