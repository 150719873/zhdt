package com.dotop.smartwater.project.third.concentrator.dao;

import com.dotop.smartwater.project.third.concentrator.core.dto.ConcentratorDto;
import com.dotop.smartwater.project.third.concentrator.core.dto.ConcentratorFileDto;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorFileVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.auth.dto.AreaDto;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;

import java.util.List;

/**
 * 集中器/中继器数据库层接口
 *
 *
 */
public interface IConcentratorDao extends BaseDao<ConcentratorDto, ConcentratorVo> {

    /**
     * 根据code查询数据
     * @param concentratorDto
     * @return
     */
    ConcentratorVo getByCode(ConcentratorDto concentratorDto);

    /**
     * 改变状态
     * @param concentratorDto
     */
    void editStatus(ConcentratorDto concentratorDto);

    /**
     * 判断是否存在对应的数据
     * @param concentratorDto
     * @return
     */
    @Override
    Boolean isExist(ConcentratorDto concentratorDto);

    /**
     * 查询全部
     * @param concentratorDto
     * @return
     */
    @Override
    List<ConcentratorVo> list(ConcentratorDto concentratorDto);

    /**
     * 删除
     * @param concentratorDto
     * @return
     */
    @Override
    Integer del(ConcentratorDto concentratorDto);

    /**
     * 更新
     * @param concentratorDto
     * @return
     */
    @Override
    Integer edit(ConcentratorDto concentratorDto);

    @Override
    ConcentratorVo get(ConcentratorDto concentratorDto);

    List<ConcentratorVo> listArchive(ConcentratorDto concentratorDto);


    @Override
    void add(ConcentratorDto concentratorDto);

    List<ConcentratorFileVo> listFile(ConcentratorFileDto concentratorFileDto);

    @Override
    void adds(List<ConcentratorDto> concentratorDtos);

    List<AreaVo> getAreaList(AreaDto areaDto);
}
