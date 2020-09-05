package com.dotop.pipe.api.dao.patrol;

import com.dotop.pipe.core.dto.patrol.PatrolRouteTemplateDto;
import com.dotop.pipe.core.vo.patrol.PatrolRouteTemplateVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;

import java.util.List;

/**
 *
 */
public interface IPatrolRouteTemplateDao extends BaseDao<PatrolRouteTemplateDto, PatrolRouteTemplateVo> {


    /**
     * 新增
     *
     * @param patrolRouteTemplateDto
     */
    @Override
    void add(PatrolRouteTemplateDto patrolRouteTemplateDto);

    /**
     * 查询全部数据
     *
     * @param patrolRouteTemplateDto
     * @return
     */
    @Override
    List<PatrolRouteTemplateVo> list(PatrolRouteTemplateDto patrolRouteTemplateDto);

    /**
     * 删除
     *
     * @param patrolRouteTemplateDto
     * @return
     */
    @Override
    Integer del(PatrolRouteTemplateDto patrolRouteTemplateDto);

    /**
     * 更新
     *
     * @param patrolRouteTemplateDto
     * @return
     */
    @Override
    Integer edit(PatrolRouteTemplateDto patrolRouteTemplateDto);

    /**
     * 获取某一条数据
     *
     * @param patrolRouteTemplateDto
     * @return
     */
    @Override
    PatrolRouteTemplateVo get(PatrolRouteTemplateDto patrolRouteTemplateDto);


}
