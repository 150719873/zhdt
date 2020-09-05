package com.dotop.smartwater.project.third.concentrator.api;

import com.dotop.smartwater.project.third.concentrator.core.vo.CollectorVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.third.concentrator.core.form.CollectorForm;

import java.util.List;

/**
 * 采集器业务逻辑接口
 *
 *
 */
public interface ICollectorFactory extends BaseFactory<CollectorForm, CollectorVo> {

    @Override
    CollectorVo add(CollectorForm collectorForm) throws FrameworkRuntimeException;

    @Override
    CollectorVo get(CollectorForm collectorForm) throws FrameworkRuntimeException;

    @Override
    Pagination<CollectorVo> page(CollectorForm collectorForm) throws FrameworkRuntimeException;

    @Override
    List<CollectorVo> list(CollectorForm collectorForm) throws FrameworkRuntimeException;

    @Override
    CollectorVo edit(CollectorForm collectorForm) throws FrameworkRuntimeException;

    CollectorVo editStatus(CollectorForm collectorForm) throws FrameworkRuntimeException;

    @Override
    String del(CollectorForm collectorForm) throws FrameworkRuntimeException;

    Pagination<CollectorVo> pageArchive(CollectorForm collectorForm) throws FrameworkRuntimeException;


    CollectorVo getByCode(CollectorForm collectorForm) throws FrameworkRuntimeException;

    boolean isExist(CollectorForm collectorForm) throws FrameworkRuntimeException;

}
