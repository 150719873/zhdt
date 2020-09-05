package com.dotop.pipe.web.api.factory.water;

import com.dotop.pipe.api.client.core.WaterDeviceForm;
import com.dotop.pipe.api.client.core.WaterDeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface IWaterDeviceFactory extends BaseFactory<WaterDeviceForm, WaterDeviceVo> {

    @Override
    Pagination<WaterDeviceVo> page(WaterDeviceForm waterDeviceForm) throws FrameworkRuntimeException;
}
