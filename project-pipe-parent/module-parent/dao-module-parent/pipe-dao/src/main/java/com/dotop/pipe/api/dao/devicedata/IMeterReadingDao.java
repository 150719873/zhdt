package com.dotop.pipe.api.dao.devicedata;

import java.util.List;

import com.dotop.pipe.core.dto.area.AreaDto;
import com.dotop.pipe.core.dto.decive.DeviceDto;
import com.dotop.pipe.core.vo.device.DeviceVo;

public interface IMeterReadingDao {

	List<DeviceVo> deviceList(DeviceDto deviceDto);

	List<DeviceVo> areaList(AreaDto areaDto);
}
