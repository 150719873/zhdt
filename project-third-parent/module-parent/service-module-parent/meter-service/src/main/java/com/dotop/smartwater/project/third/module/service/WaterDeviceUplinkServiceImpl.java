package com.dotop.smartwater.project.third.module.service;

import com.dotop.smartwater.project.third.module.api.dao.IWaterDeviceUplinkDao;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.third.module.api.service.IWaterDeviceUplinkService;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.third.module.core.water.dto.DeviceUplinkDto;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceUplinkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@Component
public class WaterDeviceUplinkServiceImpl implements IWaterDeviceUplinkService {

    @Autowired
    private IWaterDeviceUplinkDao iWaterDeviceUplinkDao;

    @Override
    public List<DeviceUplinkVo> list(DeviceUplinkBo deviceUplinkBo) throws FrameworkRuntimeException {
        DeviceUplinkDto deviceUplinkDto = BeanUtils.copy(deviceUplinkBo, DeviceUplinkDto.class);
        deviceUplinkDto.setIsDel(RootModel.NOT_DEL);
        return iWaterDeviceUplinkDao.list(deviceUplinkDto);
    }

    @Override
    public List<DeviceUplinkVo> listDegrees(DeviceUplinkBo deviceUplinkBo) throws FrameworkRuntimeException {
        DeviceUplinkDto deviceUplinkDto = BeanUtils.copy(deviceUplinkBo, DeviceUplinkDto.class);
        deviceUplinkDto.setIsDel(RootModel.NOT_DEL);
        return iWaterDeviceUplinkDao.listDegrees(deviceUplinkDto);
    }
}
