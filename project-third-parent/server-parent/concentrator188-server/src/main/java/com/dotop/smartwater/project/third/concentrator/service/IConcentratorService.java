package com.dotop.smartwater.project.third.concentrator.service;

import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorFileBo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorFileVo;
import com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.bo.AreaBo;
import com.dotop.smartwater.project.module.core.auth.vo.AreaVo;

import java.util.List;

/**
 * 集中器/中继器数据获取层接口
 *
 *
 */
public interface IConcentratorService extends BaseService<ConcentratorBo, ConcentratorVo> {

    @Override
    ConcentratorVo add(ConcentratorBo concentratorBo) throws FrameworkRuntimeException;

    @Override
    ConcentratorVo get(ConcentratorBo concentratorBo) throws FrameworkRuntimeException;

    @Override
    ConcentratorVo getByCode(ConcentratorBo concentratorBo) throws FrameworkRuntimeException;

    @Override
    Pagination<ConcentratorVo> page(ConcentratorBo concentratorBo) throws FrameworkRuntimeException;

    @Override
    List<ConcentratorVo> list(ConcentratorBo concentratorBo) throws FrameworkRuntimeException;

    @Override
    ConcentratorVo edit(ConcentratorBo concentratorBo) throws FrameworkRuntimeException;

    @Override
    String del(ConcentratorBo concentratorBo) throws FrameworkRuntimeException;

    @Override
    boolean isExist(ConcentratorBo concentratorBo) throws FrameworkRuntimeException;

    ConcentratorVo editStatus(ConcentratorBo concentratorBo);

    Pagination<ConcentratorVo> pageArchive(ConcentratorBo concentratorBo) throws FrameworkRuntimeException;

    int editReordering(ConcentratorBo concentratorBo) throws FrameworkRuntimeException;

    List<ConcentratorFileVo> listFile(ConcentratorFileBo concentratorFileBo) throws FrameworkRuntimeException;

    @Override
    void adds(List<ConcentratorBo> concentratorBos) throws FrameworkRuntimeException;

    List<AreaVo> getAreaList(AreaBo areaBo) throws FrameworkRuntimeException;
}
