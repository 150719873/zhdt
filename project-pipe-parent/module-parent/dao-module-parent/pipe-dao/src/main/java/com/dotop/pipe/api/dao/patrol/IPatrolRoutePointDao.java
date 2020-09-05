package com.dotop.pipe.api.dao.patrol;

import com.dotop.pipe.core.dto.patrol.PatrolRoutePointDto;
import com.dotop.pipe.core.vo.patrol.PatrolRoutePointVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;

import java.util.List;

/**
 *
 */
public interface IPatrolRoutePointDao extends BaseDao<PatrolRoutePointDto, PatrolRoutePointVo> {


    /**
     * 新增
     *
     * @param patrolRouteTemplateDto
     */
    @Override
    void add(PatrolRoutePointDto patrolRouteTemplateDto);

    /**
     * 查询全部数据
     *
     * @param patrolRouteTemplateDto
     * @return
     */
    @Override
    List<PatrolRoutePointVo> list(PatrolRoutePointDto patrolRouteTemplateDto);

    /**
     * 删除
     *
     * @param patrolRouteTemplateDto
     * @return
     */
    @Override
    Integer del(PatrolRoutePointDto patrolRouteTemplateDto);

    /**
     * 更新
     *
     * @param patrolRouteTemplateDto
     * @return
     */
    @Override
    Integer edit(PatrolRoutePointDto patrolRouteTemplateDto);

    /**
     * 获取某一条数据
     *
     * @param patrolRouteTemplateDto
     * @return
     */
    @Override
    PatrolRoutePointVo get(PatrolRoutePointDto patrolRouteTemplateDto);


    void batchAdd(List<PatrolRoutePointDto> pointDtoList);

    void batchUpdate(List<PatrolRoutePointDto> pointDtoList);

    void batchDel(List<PatrolRoutePointDto> pointDtoList);
}
