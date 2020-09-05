package com.dotop.smartwater.view.server.service.brustpipe;


import com.dotop.pipe.core.bo.brustpipe.BrustPipeBo;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.vo.brustpipe.BrustPipeVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.view.server.core.brustpipe.vo.BrustPipeOperationsVo;

import java.util.List;

/**
 *
 */
public interface IBrustPipeOperationsService {


    BrustPipeOperationsVo brustPipe(DeviceBo deviceBo) throws FrameworkRuntimeException;

    List<BrustPipeVo> brustPipeList(BrustPipeBo brustPipeBo) throws FrameworkRuntimeException;


    List<DeviceVo> listDevice(DeviceBo deviceBo)throws FrameworkRuntimeException;
}
