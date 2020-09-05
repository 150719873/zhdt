package com.dotop.pipe.web.api.factory.device;

import com.dotop.pipe.core.form.DeviceMappingForm;
import com.dotop.pipe.core.vo.device.DeviceMappingVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;

import java.util.List;

public interface IDeviceMappingFactory extends BaseFactory<DeviceMappingForm, DeviceMappingVo> {

    @Override
    DeviceMappingVo add(DeviceMappingForm deviceMappingForm);

    List<DeviceMappingVo> mappingList(DeviceMappingForm deviceMappingForm);

    List<DeviceMappingVo> regionMappingList(DeviceMappingForm deviceMappingForm);
}
