package com.dotop.pipe.api.dao.patrol;

import com.dotop.pipe.core.dto.patrol.PatrolTaskDto;
import com.dotop.pipe.core.vo.patrol.PatrolTaskVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IPatrolTaskDao extends BaseDao<PatrolTaskDto, PatrolTaskVo> {
    /**
     * 修改状态
     *
     * @param patrolTaskDto
     */
    void editStatus(PatrolTaskDto patrolTaskDto);


    /**
     * 新增
     *
     * @param patrolTaskDto
     */
    @Override
    void add(PatrolTaskDto patrolTaskDto);

    /**
     * 查询全部数据
     *
     * @param patrolTaskDto
     * @return
     */
    @Override
    List<PatrolTaskVo> list(PatrolTaskDto patrolTaskDto);

    /**
     * 删除
     *
     * @param patrolTaskDto
     * @return
     */
    @Override
    Integer del(PatrolTaskDto patrolTaskDto);

    /**
     * 更新
     *
     * @param patrolTaskDto
     * @return
     */
    @Override
    Integer edit(PatrolTaskDto patrolTaskDto);

    /**
     * 获取某一条数据
     *
     * @param patrolTaskDto
     * @return
     */
    @Override
    PatrolTaskVo get(PatrolTaskDto patrolTaskDto);


    void batchUpdateStatus(@Param("batList") List<PatrolTaskDto> batList, @Param("enterpriseId") String enterpriseId, @Param("status") String status);
}
