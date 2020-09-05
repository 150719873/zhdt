package com.dotop.pipe.web.api.factory.patrol;


import com.dotop.pipe.core.form.PatrolRouteTemplateForm;
import com.dotop.pipe.core.vo.patrol.PatrolRouteTemplateVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

/**
 *
 */
public interface IPatrolRouteTemplateFactory extends BaseFactory<PatrolRouteTemplateForm, PatrolRouteTemplateVo> {

    @Override
    PatrolRouteTemplateVo add(PatrolRouteTemplateForm patrolRouteTemplateForm) throws FrameworkRuntimeException;

    @Override
    PatrolRouteTemplateVo get(PatrolRouteTemplateForm patrolRouteTemplateForm) throws FrameworkRuntimeException;

    @Override
    Pagination<PatrolRouteTemplateVo> page(PatrolRouteTemplateForm patrolRouteTemplateForm) throws FrameworkRuntimeException;

    @Override
    List<PatrolRouteTemplateVo> list(PatrolRouteTemplateForm patrolRouteTemplateForm) throws FrameworkRuntimeException;

    @Override
    PatrolRouteTemplateVo edit(PatrolRouteTemplateForm patrolRouteTemplateForm) throws FrameworkRuntimeException;

    @Override
    String del(PatrolRouteTemplateForm patrolRouteTemplateForm) throws FrameworkRuntimeException;
}
