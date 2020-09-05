package com.dotop.pipe.api.dao.patrol;

import com.dotop.pipe.core.dto.patrol.PatrolRouteTaskDto;
import com.dotop.pipe.core.vo.patrol.PatrolRouteTaskVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;

import java.util.List;

public interface IPatrolRouteTaskDao extends BaseDao<PatrolRouteTaskDto, PatrolRouteTaskVo> {
    /**
     * 修改状态
     *
     * @param patrolRouteTaskDto
     */
    void editStatus(PatrolRouteTaskDto patrolRouteTaskDto);


    /**
     * 新增
     *
     * @param patrolRouteTaskDto
     */
    @Override
    void add(PatrolRouteTaskDto patrolRouteTaskDto);

    /**
     * 查询全部数据
     *
     * @param patrolRouteTaskDto
     * @return
     */
    @Override
    List<PatrolRouteTaskVo> list(PatrolRouteTaskDto patrolRouteTaskDto);

    /**
     * 删除
     *
     * @param patrolRouteTaskDto
     * @return
     */
    @Override
    Integer del(PatrolRouteTaskDto patrolRouteTaskDto);

    /**
     * 更新
     *
     * @param patrolRouteTaskDto
     * @return
     */
    @Override
    Integer edit(PatrolRouteTaskDto patrolRouteTaskDto);

    /**
     * 获取某一条数据
     *
     * @param patrolRouteTaskDto
     * @return
     */
    @Override
    PatrolRouteTaskVo get(PatrolRouteTaskDto patrolRouteTaskDto);


    void batchAdd(List<PatrolRouteTaskDto> taskDtoList);

    void batchUpdate(List<PatrolRouteTaskDto> taskDtoList);
}
