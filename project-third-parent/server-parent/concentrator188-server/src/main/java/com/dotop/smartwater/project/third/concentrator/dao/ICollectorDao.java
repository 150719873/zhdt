package com.dotop.smartwater.project.third.concentrator.dao;

import com.dotop.smartwater.project.third.concentrator.core.dto.CollectorDto;
import com.dotop.smartwater.project.third.concentrator.core.vo.CollectorVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采集器数据库层接口
 *
 *
 */
public interface ICollectorDao extends BaseDao<CollectorDto, CollectorVo> {

    /**
     * 根据code查询数据
     *
     * @param CollectorDto
     * @return
     */
    CollectorVo getByCode(CollectorDto CollectorDto);

    /**
     * 判断是否存在对应的数据
     *
     * @param concentratorDto
     * @return
     */
    @Override
    Boolean isExist(CollectorDto CollectorDto);

    /**
     * 修改状态
     *
     * @param CollectorDto
     */
    void editStatus(CollectorDto CollectorDto);

    /**
     * 新增
     *
     * @param collectorDto
     */
    @Override
    void add(CollectorDto collectorDto);

    /**
     * 查询全部数据
     *
     * @param collectorDto
     * @return
     */
    @Override
    List<CollectorVo> list(CollectorDto collectorDto);

    /**
     * 删除
     *
     * @param collectorDto
     * @return
     */
    @Override
    Integer del(CollectorDto collectorDto);

    /**
     * 更新
     *
     * @param collectorDto
     * @return
     */
    @Override
    Integer edit(CollectorDto collectorDto);

    /**
     * 获取某一条数据
     *
     * @param collectorDto
     * @return
     */
    @Override
    CollectorVo get(CollectorDto collectorDto);

    /**
     * 根据集中器id统计采集器个数
     *
     * @param id
     * @param code
     * @return
     */
    Integer countCollector(@Param("id") String id, @Param("code") String code);

    /**
     * 档案采集器分页
     *
     * @param collectorDto
     * @return
     */
    List<CollectorVo> listArchive(CollectorDto collectorDto);

    @Override
    void adds(List<CollectorDto> collectorDtos);
}
