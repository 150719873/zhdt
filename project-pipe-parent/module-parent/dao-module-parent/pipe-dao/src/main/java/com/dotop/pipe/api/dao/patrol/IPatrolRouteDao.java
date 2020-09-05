package com.dotop.pipe.api.dao.patrol;

import com.dotop.pipe.core.dto.patrol.PatrolRouteDto;
import com.dotop.pipe.core.vo.patrol.PatrolRouteVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;

import java.util.List;

public interface IPatrolRouteDao extends BaseDao<PatrolRouteDto, PatrolRouteVo> {



    /**
     * 新增
     *
     * @param patrolRouteDto
     */
    @Override
    void add(PatrolRouteDto patrolRouteDto);

    /**
     * 查询全部数据
     *
     * @param patrolRouteDto
     * @return
     */
    @Override
    List<PatrolRouteVo> list(PatrolRouteDto patrolRouteDto);

    /**
     * 删除
     *
     * @param patrolRouteDto
     * @return
     */
    @Override
    Integer del(PatrolRouteDto patrolRouteDto);

    /**
     * 更新
     *
     * @param patrolRouteDto
     * @return
     */
    @Override
    Integer edit(PatrolRouteDto patrolRouteDto);

    /**
     * 获取某一条数据
     *
     * @param patrolRouteDto
     * @return
     */
    @Override
    PatrolRouteVo get(PatrolRouteDto patrolRouteDto);



}
