package com.dotop.pipe.api.client;

import com.dotop.pipe.api.client.core.WaterDeviceVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 * 对接水务平台设备信息
 *
 *
 */
public interface IWaterDeviceClient {

    /**
     * 获取设备分页列表
     */
    Pagination<WaterDeviceVo> page(Integer page, Integer pageCount, String devno, String deveui) throws FrameworkRuntimeException;


}
