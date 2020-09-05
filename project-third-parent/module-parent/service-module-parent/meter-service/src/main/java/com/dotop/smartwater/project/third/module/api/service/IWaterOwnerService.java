package com.dotop.smartwater.project.third.module.api.service;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.third.module.core.water.vo.OwnerVo;

import java.util.List;

/**
 *
 */
public interface IWaterOwnerService extends BaseService<OwnerBo, OwnerVo> {

    @Override
    List<OwnerVo> list(OwnerBo ownerBo) throws FrameworkRuntimeException;

    List<OwnerVo> findWaterOwnerByNoList(List<String> ccidList, String enterpriseid);

    @Override
    OwnerVo get(OwnerBo ownerBo) throws FrameworkRuntimeException;
}
