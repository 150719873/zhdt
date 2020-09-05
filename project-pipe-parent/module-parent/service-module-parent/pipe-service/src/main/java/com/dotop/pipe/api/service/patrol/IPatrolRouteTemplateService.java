package com.dotop.pipe.api.service.patrol;


import com.dotop.pipe.core.bo.patrol.PatrolRouteTemplateBo;
import com.dotop.pipe.core.vo.patrol.PatrolRouteTemplateVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

/**
 *
 */
public interface IPatrolRouteTemplateService extends BaseService<PatrolRouteTemplateBo, PatrolRouteTemplateVo> {


    @Override
    PatrolRouteTemplateVo add(PatrolRouteTemplateBo patrolRouteTemplateBo) throws FrameworkRuntimeException;

    @Override
    PatrolRouteTemplateVo get(PatrolRouteTemplateBo patrolRouteTemplateBo) throws FrameworkRuntimeException;

    @Override
    Pagination<PatrolRouteTemplateVo> page(PatrolRouteTemplateBo patrolRouteTemplateBo) throws FrameworkRuntimeException;

    @Override
    List<PatrolRouteTemplateVo> list(PatrolRouteTemplateBo patrolRouteTemplateBo) throws FrameworkRuntimeException;

    @Override
    PatrolRouteTemplateVo edit(PatrolRouteTemplateBo patrolRouteTemplateBo) throws FrameworkRuntimeException;

    @Override
    String del(PatrolRouteTemplateBo patrolRouteTemplateBo) throws FrameworkRuntimeException;

}
