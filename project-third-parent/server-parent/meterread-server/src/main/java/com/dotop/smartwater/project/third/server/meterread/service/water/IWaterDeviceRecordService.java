package com.dotop.smartwater.project.third.server.meterread.service.water;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.water.bo.DeviceRecordBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceRecordVo;

/**
 * 对接水务系统的换表数据接口
 *
 *
 */
public interface IWaterDeviceRecordService extends BaseService<DeviceRecordBo, DeviceRecordVo> {

    /**
     * 查询业主的换表记录，条件水司、业主
     *
     * @param deviceRecordBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    DeviceRecordVo get(DeviceRecordBo deviceRecordBo) throws FrameworkRuntimeException;


}
