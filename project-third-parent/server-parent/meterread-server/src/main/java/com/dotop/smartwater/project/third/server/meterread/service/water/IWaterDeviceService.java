package com.dotop.smartwater.project.third.server.meterread.service.water;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.bo.EnterpriseBo;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;

/**
 * 对接水务系统的设备数据接口
 *
 *
 */
public interface IWaterDeviceService extends BaseService<DeviceBo, DeviceVo> {


    EnterpriseVo checkEnterpriseId(EnterpriseBo enterpriseBo);
}
