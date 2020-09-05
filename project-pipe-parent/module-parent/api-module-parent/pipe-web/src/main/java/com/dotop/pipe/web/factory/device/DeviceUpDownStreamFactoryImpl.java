package com.dotop.pipe.web.factory.device;

import java.util.Date;

import com.dotop.pipe.web.api.factory.device.IDeviceUpDownStreamFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.pipe.api.service.device.IDeviceService;
import com.dotop.pipe.api.service.device.IDeviceUpDownStreamService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.bo.device.DeviceUpDownStreamBo;
import com.dotop.pipe.core.form.DeviceUpDownStreamForm;
import com.dotop.pipe.core.vo.device.DeviceUpDownStreamVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;

/**
 *
 * @date 2019年2月15日
 */
@Component
public class DeviceUpDownStreamFactoryImpl implements IDeviceUpDownStreamFactory {

    private static final Logger logger = LogManager.getLogger(DeviceUpDownStreamFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IDeviceUpDownStreamService iDeviceUpDownStreamService;

    @Autowired
    private IDeviceService iDeviceService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public DeviceUpDownStreamVo add(DeviceUpDownStreamForm deviceUpDownStreamForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        DeviceUpDownStreamBo deviceUpDownStreamBo = BeanUtils.copyProperties(deviceUpDownStreamForm,
                DeviceUpDownStreamBo.class);
        deviceUpDownStreamBo.setCurr(new Date());
        deviceUpDownStreamBo.setUserBy(userBy);
        deviceUpDownStreamBo.setEnterpriseId(operEid);
        iDeviceUpDownStreamService.add(deviceUpDownStreamBo);
        return null;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void editAlarmProperty(DeviceUpDownStreamForm deviceUpDownStreamForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        DeviceUpDownStreamBo deviceUpDownStreamBo = BeanUtils.copyProperties(deviceUpDownStreamForm,
                DeviceUpDownStreamBo.class);
        deviceUpDownStreamBo.setCurr(new Date());
        deviceUpDownStreamBo.setUserBy(userBy);
        deviceUpDownStreamBo.setEnterpriseId(operEid);
        iDeviceUpDownStreamService.editAlarmProperty(deviceUpDownStreamBo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String del(DeviceUpDownStreamForm deviceUpDownStreamForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        DeviceUpDownStreamBo deviceUpDownStreamBo = BeanUtils.copyProperties(deviceUpDownStreamForm,
                DeviceUpDownStreamBo.class);
        deviceUpDownStreamBo.setCurr(new Date());
        deviceUpDownStreamBo.setUserBy(userBy);
        deviceUpDownStreamBo.setEnterpriseId(operEid);
        iDeviceUpDownStreamService.del(deviceUpDownStreamBo);
        return null;
    }

    @Override
    public DeviceUpDownStreamVo get(DeviceUpDownStreamForm deviceUpDownStreamForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        DeviceUpDownStreamBo deviceUpDownStreamBo = BeanUtils.copyProperties(deviceUpDownStreamForm,
                DeviceUpDownStreamBo.class);
        deviceUpDownStreamBo.setEnterpriseId(operEid);
        DeviceUpDownStreamVo streamVo = new DeviceUpDownStreamVo();
        streamVo.setDeviceParentList(iDeviceUpDownStreamService.getParent(deviceUpDownStreamBo));
        streamVo.setDeviceChildList(iDeviceUpDownStreamService.getChild(deviceUpDownStreamBo));
        DeviceBo deviceBo = new DeviceBo();
        deviceBo.setDeviceId(deviceUpDownStreamForm.getDeviceId());
        deviceBo.setEnterpriseId(operEid);
        streamVo.setDeviceVo(iDeviceService.get(deviceBo));
        return streamVo;
    }

    @Override
    public Pagination<DeviceUpDownStreamVo> page(DeviceUpDownStreamForm deviceUpDownStreamForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        DeviceUpDownStreamBo deviceUpDownStreamBo = BeanUtils.copyProperties(deviceUpDownStreamForm,
                DeviceUpDownStreamBo.class);
        deviceUpDownStreamBo.setEnterpriseId(operEid);
        return iDeviceUpDownStreamService.page(deviceUpDownStreamBo);
    }
}
