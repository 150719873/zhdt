package com.dotop.smartwater.project.third.module.api.service;

import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.auth.bo.EnterpriseBo;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceBo;

import java.util.List;

/**
 *
 */
public interface IWaterDeviceService extends BaseService<DeviceBo, DeviceVo> {

    @Override
    List<DeviceVo> list(DeviceBo deviceBo) throws FrameworkRuntimeException;

    @Override
    DeviceVo get(DeviceBo deviceBo) throws FrameworkRuntimeException;

    EnterpriseVo checkEnterpriseId(EnterpriseBo enterpriseBo);
}
