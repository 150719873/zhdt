package com.dotop.pipe.web.api.factory.patrol;


import com.dotop.pipe.core.form.PatrolRouteForm;
import com.dotop.pipe.core.vo.patrol.PatrolRouteVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

public interface IPatrolRouteFactory extends BaseFactory<PatrolRouteForm, PatrolRouteVo> {

    @Override
    PatrolRouteVo add(PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException;

    PatrolRouteVo addWithOutAuth(PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException;

    @Override
    PatrolRouteVo get(PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException;

    @Override
    Pagination<PatrolRouteVo> page(PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException;

    @Override
    List<PatrolRouteVo> list(PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException;

    @Override
    PatrolRouteVo edit(PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException;

    PatrolRouteVo editWithOutAuth(PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException;

    @Override
    String del(PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException;
}
