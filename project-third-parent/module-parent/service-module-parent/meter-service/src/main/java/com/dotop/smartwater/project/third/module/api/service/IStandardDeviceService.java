package com.dotop.smartwater.project.third.module.api.service;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;

/**
 *
 */
public interface IStandardDeviceService {


    Pagination<DeviceVo> pageDevice(DeviceBo deviceBo, Integer page, Integer pageCount) throws FrameworkRuntimeException;
}
