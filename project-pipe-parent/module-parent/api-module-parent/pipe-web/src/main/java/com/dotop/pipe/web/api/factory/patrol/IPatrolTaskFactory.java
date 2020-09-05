package com.dotop.pipe.web.api.factory.patrol;


import com.dotop.pipe.core.form.PatrolTaskForm;
import com.dotop.pipe.core.vo.patrol.PatrolTaskVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

public interface IPatrolTaskFactory extends BaseFactory<PatrolTaskForm, PatrolTaskVo> {

    @Override
    PatrolTaskVo add(PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException;

    PatrolTaskVo addWithOutAuth (PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException;

    @Override
    PatrolTaskVo get(PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException;

    @Override
    Pagination<PatrolTaskVo> page(PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException;

    @Override
    List<PatrolTaskVo> list(PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException;

    @Override
    PatrolTaskVo edit(PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException;

    @Override
    String del(PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException;

    PatrolTaskVo editStatus(PatrolTaskForm patrolTaskForm);
}
