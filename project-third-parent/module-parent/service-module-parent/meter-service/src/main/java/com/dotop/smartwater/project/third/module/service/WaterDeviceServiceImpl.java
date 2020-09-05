package com.dotop.smartwater.project.third.module.service;

import com.dotop.smartwater.project.third.module.api.dao.IWaterDao;
import com.dotop.smartwater.project.third.module.api.dao.IWaterDeviceDao;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.auth.bo.EnterpriseBo;
import com.dotop.smartwater.project.module.core.auth.dto.EnterpriseDto;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.third.module.api.service.IWaterDeviceService;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.third.module.core.water.dto.DeviceDto;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@Component
public class WaterDeviceServiceImpl implements IWaterDeviceService {

    @Autowired
    private IWaterDeviceDao iWaterDeviceDao;
    @Autowired
    IWaterDao iWaterDao;


    @Override
    public List<DeviceVo> list(DeviceBo deviceBo) throws FrameworkRuntimeException {
        DeviceDto deviceDto = BeanUtils.copy(deviceBo, DeviceDto.class);
        deviceDto.setIsDel(RootModel.NOT_DEL);
        return iWaterDeviceDao.list(deviceDto);
    }

    @Override
    public DeviceVo get(DeviceBo deviceBo) throws FrameworkRuntimeException {
        DeviceDto deviceDto = BeanUtils.copy(deviceBo, DeviceDto.class);
        deviceDto.setIsDel(RootModel.NOT_DEL);
        return iWaterDeviceDao.get(deviceDto);
    }

    @Override
    public EnterpriseVo checkEnterpriseId(EnterpriseBo enterpriseBo) {
        EnterpriseDto enterpriseDto = com.dotop.smartwater.dependence.core.utils.BeanUtils.copy(enterpriseBo, EnterpriseDto.class);
        return iWaterDao.checkEnterpriseId(enterpriseDto);
    }
}
