package com.dotop.pipe.web.factory.report;

import com.dotop.pipe.web.api.factory.report.IDeviceCurrFactory;
import com.dotop.pipe.api.service.device.IDeviceService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.vo.report.DeviceCurrVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @date
 */
@Component
public class DeviceCurrFactoryImpl implements IDeviceCurrFactory {

    private final static Logger logger = LogManager.getLogger(DeviceCurrFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IDeviceService iDeviceService;

    @Override
    public Pagination<DeviceCurrVo> page(DeviceForm deviceForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        DeviceBo deviceBo = BeanUtils.copy(deviceForm, DeviceBo.class);
        deviceBo.setEnterpriseId(loginCas.getEnterpriseId());
        return iDeviceService.getDeviceCurrPropertys(deviceBo);
    }
}
