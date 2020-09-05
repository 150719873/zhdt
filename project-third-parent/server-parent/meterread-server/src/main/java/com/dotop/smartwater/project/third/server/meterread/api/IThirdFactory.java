package com.dotop.smartwater.project.third.server.meterread.api;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.water.bo.DeviceDownlinkBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;

import java.util.List;

/**
 * 第三方系统业务逻辑处理层
 *
 *
 */
public interface IThirdFactory {


    List<DeviceDownlinkVo> getDownlink(List<DeviceDownlinkBo> deviceDownlinkBos) throws FrameworkRuntimeException;
}
