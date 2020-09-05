package com.dotop.pipe.api.service.device;

import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.bo.device.DeviceMappingBo;
import com.dotop.pipe.core.vo.device.DeviceMappingVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseService;

import java.util.List;

public interface IDeviceMappingService extends BaseService<DeviceBo, DeviceVo> {

    DeviceVo add(DeviceBo deviceBo);

    String del(DeviceBo deviceBo);

    /**
     * 新增关联关系  可以是正向新增 或者 反向新增
     *
     * @return
     */
    DeviceMappingVo addMapping(DeviceMappingBo deviceMappingBo);

    /**
     * 删除关联关系 可以是正向删除 或者是 反向删除
     *
     * @return
     */
    DeviceMappingVo delMapping(DeviceMappingBo deviceMappingBo);

    List<DeviceMappingVo> mappingList(DeviceMappingBo deviceMappingBo);
    
    List<DeviceMappingVo> regionMappingList(DeviceMappingBo deviceMappingBo);
}
