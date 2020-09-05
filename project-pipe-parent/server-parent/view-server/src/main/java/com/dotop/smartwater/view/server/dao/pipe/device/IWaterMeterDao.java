package com.dotop.smartwater.view.server.dao.pipe.device;


import com.dotop.pipe.core.dto.decive.DeviceDto;
import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.pipe.core.vo.workorder.WorkOrderVo;

import java.util.List;

/**
 *
 */
public interface IWaterMeterDao {


    DevicePropertyVo waterMeterData(DeviceDto deviceDto);

    List<WorkOrderVo> workOrderList(DeviceDto deviceDto);
}
