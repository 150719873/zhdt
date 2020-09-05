package com.dotop.smartwater.project.third.module.api.service;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.water.bo.DeviceDownlinkBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;

import java.util.List;

/**
 * 对接水务系统的下行命令接口
 *
 *
 */
public interface IWaterDeviceDownlinkService extends BaseService<DeviceDownlinkBo, DeviceDownlinkVo> {

    /**
     * 根据下发命令id查询下行数据反馈查询结果；条件下发命令id；
     * @param clientIds
     * @return
     * @throws FrameworkRuntimeException
     */
    List<DeviceDownlinkVo> list(List<String> clientIds) throws FrameworkRuntimeException;
}
