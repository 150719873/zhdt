package com.dotop.smartwater.project.third.server.meterread.service.water.impl;

import com.dotop.smartwater.project.module.core.auth.bo.EnterpriseBo;
import com.dotop.smartwater.project.module.core.auth.dto.EnterpriseDto;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.third.server.meterread.dao.water.IWaterDao;
import com.dotop.smartwater.project.third.server.meterread.service.water.IWaterDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WaterDeviceServiceImpl implements IWaterDeviceService {

    @Autowired
    IWaterDao iWaterDao;


    @Override
    public EnterpriseVo checkEnterpriseId(EnterpriseBo enterpriseBo) {
        EnterpriseDto enterpriseDto = com.dotop.smartwater.dependence.core.utils.BeanUtils.copy(enterpriseBo, EnterpriseDto.class);
        return iWaterDao.checkEnterpriseId(enterpriseDto);
    }
}
