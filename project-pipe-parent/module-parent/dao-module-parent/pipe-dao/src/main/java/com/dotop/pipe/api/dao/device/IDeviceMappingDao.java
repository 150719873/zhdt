package com.dotop.pipe.api.dao.device;

import com.dotop.pipe.core.dto.decive.DeviceDto;
import com.dotop.pipe.core.dto.decive.DeviceMappingDto;
import com.dotop.pipe.core.dto.point.PointDto;
import com.dotop.pipe.core.vo.device.DeviceMappingVo;
import com.dotop.pipe.core.vo.point.PointVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;

import java.util.List;

public interface IDeviceMappingDao extends BaseDao<PointDto, PointVo> {

    void del(DeviceDto deviceDto);

    void add(DeviceDto deviceDto);

    void addMapping(DeviceMappingDto deviceMappingDto);

    void delMapping(DeviceMappingDto deviceMappingDto);

    List<DeviceMappingVo> mappingList(DeviceMappingDto deviceMappingDto);

    List<DeviceMappingVo> regionMappingList(DeviceMappingDto deviceMappingDto);
}
