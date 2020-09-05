package com.dotop.pipe.api.service.device;

import com.dotop.pipe.core.bo.report.DeviceBrustPipeBo;
import com.dotop.pipe.core.vo.report.DeviceBrustPipeVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 * 设备
 *
 *
 */
public interface IDeviceBrustPipeService extends BaseService<DeviceBrustPipeBo, DeviceBrustPipeVo> {

    Pagination<DeviceBrustPipeVo> pagePipe(DeviceBrustPipeBo deviceBrustPipeBo) throws FrameworkRuntimeException;

    Pagination<DeviceBrustPipeVo> pageArea(DeviceBrustPipeBo deviceBrustPipeBo) throws FrameworkRuntimeException;
}
