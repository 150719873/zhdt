package com.dotop.smartwater.project.third.module.api.service;

import com.dotop.smartwater.project.third.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.bo.OwnerBo;

import java.util.List;

/**
 *
 */
public interface IMeterOwnerService extends BaseService<OwnerBo, OwnerVo> {

    @Override
    List<OwnerVo> list(OwnerBo ownerBo) throws FrameworkRuntimeException;

    @Override
    void adds(List<OwnerBo> ownerBos) throws FrameworkRuntimeException;


    void edits(List<OwnerBo> ownerBos, String enterpriseid) throws FrameworkRuntimeException;

    @Override
    OwnerVo get(OwnerBo ownerBo) throws FrameworkRuntimeException;
}
