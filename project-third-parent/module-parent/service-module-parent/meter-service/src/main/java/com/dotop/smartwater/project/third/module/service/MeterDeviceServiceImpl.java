package com.dotop.smartwater.project.third.module.service;

import com.dotop.smartwater.project.third.module.api.dao.IMeterDeviceDao;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.third.module.api.service.IMeterDeviceService;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.third.module.core.water.dto.DeviceDto;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
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
public class MeterDeviceServiceImpl implements IMeterDeviceService {

    @Autowired
    private IMeterDeviceDao iMeterDeviceDao;

    @Override
    public List<DeviceVo> list(DeviceBo deviceBo) throws FrameworkRuntimeException {
        DeviceDto deviceDto = BeanUtils.copy(deviceBo, DeviceDto.class);
        deviceDto.setIsDel(RootModel.NOT_DEL);
        return iMeterDeviceDao.list(deviceDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void adds(List<DeviceBo> deviceBos) throws FrameworkRuntimeException {
        Date curr = new Date();
        List<DeviceDto> deviceDtos = BeanUtils.copy(deviceBos, DeviceDto.class);
        deviceDtos.forEach(deviceDto -> {
            deviceDto.setIsDel(RootModel.NOT_DEL);
            deviceDto.setCurr(curr);
        });
        iMeterDeviceDao.adds(deviceDtos);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void edits(List<DeviceBo> deviceBos) throws FrameworkRuntimeException {
        List<DeviceDto> deviceDtos = BeanUtils.copy(deviceBos, DeviceDto.class);
        Date curr = new Date();
        deviceDtos.forEach(deviceDto -> {
            deviceDto.setCurr(curr);
        });
        iMeterDeviceDao.edits(deviceDtos);
    }

    @Override
    public DeviceVo get(DeviceBo deviceBo) throws FrameworkRuntimeException {
        DeviceDto deviceDto = BeanUtils.copy(deviceBo, DeviceDto.class);
        deviceDto.setIsDel(RootModel.NOT_DEL);
        return iMeterDeviceDao.get(deviceDto);
    }
}
