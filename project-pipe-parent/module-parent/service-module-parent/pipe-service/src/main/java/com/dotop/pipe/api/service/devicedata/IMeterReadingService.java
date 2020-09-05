package com.dotop.pipe.api.service.devicedata;

import com.dotop.pipe.core.bo.area.AreaBo;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface IMeterReadingService {

	Pagination<DeviceVo> devicePage(DeviceBo deviceBo);

	Pagination<DeviceVo> areaPage(AreaBo areaBo);

}
