package com.dotop.pipe.web.api.factory.patrol;


import com.dotop.pipe.core.form.PatrolRoutePointForm;
import com.dotop.pipe.core.vo.patrol.PatrolRoutePointVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

/**
 *
 */
public interface IPatrolRoutePointFactory extends BaseFactory<PatrolRoutePointForm, PatrolRoutePointVo> {

    @Override
    PatrolRoutePointVo add(PatrolRoutePointForm patrolRoutePointForm) throws FrameworkRuntimeException;

    @Override
    PatrolRoutePointVo get(PatrolRoutePointForm patrolRoutePointForm) throws FrameworkRuntimeException;

    @Override
    Pagination<PatrolRoutePointVo> page(PatrolRoutePointForm patrolRoutePointForm) throws FrameworkRuntimeException;

    @Override
    List<PatrolRoutePointVo> list(PatrolRoutePointForm patrolRoutePointForm) throws FrameworkRuntimeException;

    @Override
    PatrolRoutePointVo edit(PatrolRoutePointForm patrolRoutePointForm) throws FrameworkRuntimeException;

    @Override
    String del(PatrolRoutePointForm patrolRoutePointForm) throws FrameworkRuntimeException;

    void batchAdd(List<PatrolRoutePointForm> points);

    void batchAddWithOutAuth(List<PatrolRoutePointForm> points);

    void batUpdate(List<PatrolRoutePointForm> points);

    void batchDel(List<PatrolRoutePointForm> points);

    void batchDelWithOutAuth(List<PatrolRoutePointForm> points);
}
