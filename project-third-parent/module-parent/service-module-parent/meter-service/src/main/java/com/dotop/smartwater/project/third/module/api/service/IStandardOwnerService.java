package com.dotop.smartwater.project.third.module.api.service;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.third.module.core.third.standard.vo.StandardOwnerVo;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceUplinkBo;

/**
 *
 */
public interface IStandardOwnerService {


    Pagination<StandardOwnerVo> pageOwner(DeviceUplinkBo deviceUplinkBo, Integer page, Integer pageCount) throws FrameworkRuntimeException;


}
