package com.dotop.pipe.web.factory.devicedata;

import com.dotop.pipe.web.api.factory.devicedata.IMeterReadingFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.pipe.api.service.devicedata.IMeterReadingService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.area.AreaBo;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.form.AreaForm;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;

/**
 */
@Component
public class MeterReadingFactoryImpl implements IMeterReadingFactory {

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IMeterReadingService iMeterReadingService;

    @Override
    public Pagination<DeviceVo> devicePage(DeviceForm deviceForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        // 封装参数
        DeviceBo deviceBo = BeanUtils.copyProperties(deviceForm, DeviceBo.class);
        deviceBo.setEnterpriseId(operEid);
        deviceBo.setProductCategory(deviceForm.getProductCategory()); // 产品类别
        Pagination<DeviceVo> pagination = iMeterReadingService.devicePage(deviceBo);
        return pagination;
    }

    @Override
    public Pagination<DeviceVo> areaPage(AreaForm deviceForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        // 封装参数
        AreaBo areaBo = BeanUtils.copyProperties(deviceForm, AreaBo.class);
        areaBo.setEnterpriseId(operEid);
        Pagination<DeviceVo> pagination = iMeterReadingService.areaPage(areaBo);
        return pagination;
    }

}
