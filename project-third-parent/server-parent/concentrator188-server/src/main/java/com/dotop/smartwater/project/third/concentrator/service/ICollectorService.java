package com.dotop.smartwater.project.third.concentrator.service;

import com.dotop.smartwater.project.third.concentrator.core.bo.CollectorBo;
import com.dotop.smartwater.project.third.concentrator.core.vo.CollectorVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

/**
 * 采集器数据获取层接口
 *
 *
 */
public interface ICollectorService extends BaseService<CollectorBo, CollectorVo> {

    @Override
    CollectorVo add(CollectorBo collectorBo) throws FrameworkRuntimeException;

    @Override
    CollectorVo get(CollectorBo collectorBo) throws FrameworkRuntimeException;

    @Override
    CollectorVo getByCode(CollectorBo collectorBo) throws FrameworkRuntimeException;

    @Override
    Pagination<CollectorVo> page(CollectorBo collectorBo) throws FrameworkRuntimeException;

    @Override
    List<CollectorVo> list(CollectorBo collectorBo) throws FrameworkRuntimeException;

    @Override
    CollectorVo edit(CollectorBo collectorBo) throws FrameworkRuntimeException;

    @Override
    String del(CollectorBo collectorBo) throws FrameworkRuntimeException;

    @Override
    boolean isExist(CollectorBo collectorBo) throws FrameworkRuntimeException;

    CollectorVo editStatus(CollectorBo collectorBo);

    Integer countCollector(String id, String code)throws FrameworkRuntimeException;

    Pagination<CollectorVo> pageArchive(CollectorBo collectorBo) throws FrameworkRuntimeException;

    @Override
    void adds(List<CollectorBo> collectorBos) throws FrameworkRuntimeException;
}
