package com.dotop.pipe.api.dao.patrol;

import com.dotop.pipe.core.dto.patrol.PatrolTaskTimerDto;
import com.dotop.pipe.core.vo.patrol.PatrolTaskTimerVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;

import java.util.List;

public interface IPatrolTaskTimerDao extends BaseDao<PatrolTaskTimerDto, PatrolTaskTimerVo> {
    /**
     * 修改状态
     *
     * @param patrolTaskTimerDto
     */
    void editStatus(PatrolTaskTimerDto patrolTaskTimerDto);


    /**
     * 新增
     *
     * @param patrolTaskTimerDto
     */
    @Override
    void add(PatrolTaskTimerDto patrolTaskTimerDto);

    /**
     * 查询全部数据
     *
     * @param patrolTaskTimerDto
     * @return
     */
    @Override
    List<PatrolTaskTimerVo> list(PatrolTaskTimerDto patrolTaskTimerDto);

    /**
     * 删除
     *
     * @param patrolTaskTimerDto
     * @return
     */
    @Override
    Integer del(PatrolTaskTimerDto patrolTaskTimerDto);

    /**
     * 更新
     *
     * @param patrolTaskTimerDto
     * @return
     */
    @Override
    Integer edit(PatrolTaskTimerDto patrolTaskTimerDto);

    /**
     * 获取某一条数据
     *
     * @param patrolTaskTimerDto
     * @return
     */
    @Override
    PatrolTaskTimerVo get(PatrolTaskTimerDto patrolTaskTimerDto);
}
