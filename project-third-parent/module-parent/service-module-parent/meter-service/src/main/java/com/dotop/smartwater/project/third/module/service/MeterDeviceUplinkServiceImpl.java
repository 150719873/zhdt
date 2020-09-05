package com.dotop.smartwater.project.third.module.service;

import com.dotop.smartwater.project.third.module.api.dao.IMeterDeviceUplinkDao;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.third.module.api.service.IMeterDeviceUplinkService;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.third.module.core.water.dto.DeviceUplinkDto;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceUplinkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *
 */
@Component
public class MeterDeviceUplinkServiceImpl implements IMeterDeviceUplinkService {

    @Autowired
    private IMeterDeviceUplinkDao iMeterDeviceUplinkDao;


    @Override
    public List<DeviceUplinkVo> list(DeviceUplinkBo deviceUplinkBo) throws FrameworkRuntimeException {
        DeviceUplinkDto deviceUplinkDto = BeanUtils.copy(deviceUplinkBo, DeviceUplinkDto.class);
        deviceUplinkDto.setIsDel(RootModel.NOT_DEL);
        return iMeterDeviceUplinkDao.list(deviceUplinkDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void adds(List<DeviceUplinkBo> deviceUplinkBos) throws FrameworkRuntimeException {
        Date curr = new Date();
        List<DeviceUplinkDto> deviceUplinkDtos = BeanUtils.copy(deviceUplinkBos, DeviceUplinkDto.class);
        deviceUplinkDtos.forEach(deviceUplinkDto -> {
            deviceUplinkDto.setIsDel(RootModel.NOT_DEL);
            deviceUplinkDto.setCurr(curr);
        });
        iMeterDeviceUplinkDao.adds(deviceUplinkDtos);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void edits(List<DeviceUplinkBo> deviceUplinkBos) throws FrameworkRuntimeException {
        Date curr = new Date();
        List<DeviceUplinkDto> deviceUplinkDtos = BeanUtils.copy(deviceUplinkBos, DeviceUplinkDto.class);
        deviceUplinkDtos.forEach(deviceUplinkDto -> {
            deviceUplinkDto.setCurr(curr);
        });
        iMeterDeviceUplinkDao.edits(deviceUplinkDtos);
    }
}
