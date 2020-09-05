package com.dotop.pipe.web.factory.report;

import com.dotop.pipe.web.api.factory.report.IDeviceBrustPipeFactory;
import com.dotop.pipe.api.service.device.IDeviceBrustPipeService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.report.DeviceBrustPipeBo;
import com.dotop.pipe.core.form.DeviceBrustPipeForm;
import com.dotop.pipe.core.vo.report.DeviceBrustPipeVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @date
 */
@Component
public class DeviceBrustPipeFactoryImpl implements IDeviceBrustPipeFactory {

    private final static Logger logger = LogManager.getLogger(DeviceBrustPipeFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IDeviceBrustPipeService iDeviceBrustPipeService;


    @Override
    public Pagination<DeviceBrustPipeVo> pagePipe(DeviceBrustPipeForm deviceBrustPipeForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        DeviceBrustPipeBo deviceBrustPipeBo = BeanUtils.copy(deviceBrustPipeForm, DeviceBrustPipeBo.class);
        deviceBrustPipeBo.setEnterpriseId(loginCas.getEnterpriseId());
        int countDays = DateUtils.daysBetween(deviceBrustPipeBo.getStartDate(), deviceBrustPipeBo.getEndDate()) + 1;
        Pagination<DeviceBrustPipeVo> deviceBrustPipeVoPagination = iDeviceBrustPipeService.pagePipe(deviceBrustPipeBo);
        for (DeviceBrustPipeVo datum : deviceBrustPipeVoPagination.getData()) {
            datum.setCountDays(countDays);
        }
        return deviceBrustPipeVoPagination;
    }

    @Override
    public Pagination<DeviceBrustPipeVo> pageArea(DeviceBrustPipeForm deviceBrustPipeForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        DeviceBrustPipeBo deviceBrustPipeBo = BeanUtils.copy(deviceBrustPipeForm, DeviceBrustPipeBo.class);
        deviceBrustPipeBo.setEnterpriseId(loginCas.getEnterpriseId());
        int countDays = DateUtils.daysBetween(deviceBrustPipeBo.getStartDate(), deviceBrustPipeBo.getEndDate()) + 1;
        Pagination<DeviceBrustPipeVo> deviceBrustPipeVoPagination = iDeviceBrustPipeService.pageArea(deviceBrustPipeBo);
        for (DeviceBrustPipeVo datum : deviceBrustPipeVoPagination.getData()) {
            datum.setCountDays(countDays);
        }
        return deviceBrustPipeVoPagination;
    }
}
