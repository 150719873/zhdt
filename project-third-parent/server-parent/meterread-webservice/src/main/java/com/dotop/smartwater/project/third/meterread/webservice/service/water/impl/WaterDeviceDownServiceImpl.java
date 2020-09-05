package com.dotop.smartwater.project.third.meterread.webservice.service.water.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.water.bo.DeviceDownlinkBo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;
import com.dotop.smartwater.project.third.meterread.webservice.dao.water.IWaterDao;
import com.dotop.smartwater.project.third.meterread.webservice.service.water.IWaterDeviceDownlinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 对接水务系统的设备下行数据数据接口
 *
 *
 */

@Service
public class WaterDeviceDownServiceImpl implements IWaterDeviceDownlinkService {

    @Autowired
    private IWaterDao iWaterDao;

    /**
     * 根据client批量查询下行数据
     */
    @Override
    public List<DeviceDownlinkVo> list(List<DeviceDownlinkBo> deviceDownlinkBos) throws FrameworkRuntimeException {
        try {
            return iWaterDao.getDownLinkData(deviceDownlinkBos);
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
