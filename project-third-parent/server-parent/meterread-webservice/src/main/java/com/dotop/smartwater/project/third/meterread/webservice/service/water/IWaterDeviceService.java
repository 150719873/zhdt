package com.dotop.smartwater.project.third.meterread.webservice.service.water;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;

/**
 * 对接水务系统的设备数据接口
 *
 *
 */
public interface IWaterDeviceService extends BaseService<DeviceBo, DeviceVo> {

    Pagination<DeviceVo> find(DeviceBo deviceBo);

    @Override
    DeviceVo get(DeviceBo deviceBo);
}
