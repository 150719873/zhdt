package com.dotop.pipe.web.api.factory.report;

import com.dotop.pipe.core.form.DeviceBrustPipeForm;
import com.dotop.pipe.core.vo.report.DeviceBrustPipeVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface IDeviceBrustPipeFactory extends BaseFactory<DeviceBrustPipeForm, DeviceBrustPipeVo> {

    /**
     * 根据管道划分爆管信息，并分页
     * @param deviceBrustPipeForm
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<DeviceBrustPipeVo> pagePipe(DeviceBrustPipeForm deviceBrustPipeForm) throws FrameworkRuntimeException;

    /**
     * 根据区域划分爆管信息，并分页
     * @param deviceBrustPipeForm
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<DeviceBrustPipeVo> pageArea(DeviceBrustPipeForm deviceBrustPipeForm) throws FrameworkRuntimeException;
}
