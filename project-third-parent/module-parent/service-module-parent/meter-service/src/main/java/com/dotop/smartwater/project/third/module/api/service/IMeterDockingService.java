package com.dotop.smartwater.project.third.module.api.service;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.module.core.water.bo.DockingBo;
import com.dotop.smartwater.project.third.module.core.water.vo.DockingVo;

import java.util.List;

/**
 *
 */
public interface IMeterDockingService extends BaseService<DockingBo, DockingVo> {

    List<DockingVo> list(DockingBo dockingBo) throws FrameworkRuntimeException;

    @Override
    DockingVo get(DockingBo dockingBo) throws FrameworkRuntimeException;
}
