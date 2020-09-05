package com.dotop.smartwater.view.server.api.factory.view.impl;

import com.dotop.smartwater.view.server.core.enums.SecurityEnum;
import com.dotop.smartwater.view.server.core.security.vo.SecurityLogVo;
import com.dotop.smartwater.view.server.core.security.vo.SecuritySwitchVo;
import com.dotop.smartwater.view.server.service.security.ISecurityService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.view.server.api.factory.view.ISecurityFactory;
import com.dotop.smartwater.view.server.core.security.form.SecurityLogForm;
import com.dotop.smartwater.view.server.core.security.form.SecuritySwitchForm;
import com.dotop.smartwater.view.server.core.device.form.DeviceForm;
import com.dotop.smartwater.view.server.core.device.vo.DeviceVo;
import com.dotop.smartwater.view.server.service.device.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.dotop.smartwater.dependence.core.common.RootModel.NOT_DEL;
import static com.dotop.smartwater.view.server.constants.ViewConstants.WATER_FACTORY;

@Component
public class SecurityFactoryImpl implements ISecurityFactory {

    @Autowired
    private IAuthCasWeb iAuthCasWeb;

    @Autowired
    private ISecurityService iSecurityService;

    @Autowired
    private IDeviceService iDeviceService;

    @Override
    public Pagination<SecuritySwitchVo> list(SecuritySwitchForm securitySwitchForm) throws FrameworkRuntimeException {
        String enterpriseId = iAuthCasWeb.get().getEnterpriseId();
        securitySwitchForm.setEnterpriseId(enterpriseId);
        Pagination<SecuritySwitchVo> list = iSecurityService.list(securitySwitchForm);
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String edit(SecuritySwitchForm securitySwitchForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasWeb.get();
        String enterpriseId = loginCas.getEnterpriseId();
        securitySwitchForm.setEnterpriseId(enterpriseId);
        securitySwitchForm.setCurr(new Date());
        securitySwitchForm.setUserBy(loginCas.getUserName());
        return iSecurityService.edit(securitySwitchForm);
    }

    /**
     * 初始化 安防开关数据
     *
     * @throws FrameworkRuntimeException
     */
    @Override
    public void init(String enterpriseId) throws FrameworkRuntimeException {
        DeviceForm deviceForm = new DeviceForm();
        deviceForm.setEnterpriseId(enterpriseId);
        deviceForm.setProductCategory(WATER_FACTORY);
        deviceForm.setIsDel(NOT_DEL);
        List<DeviceVo> waterFactoryList = iDeviceService.list(deviceForm);
        List<SecuritySwitchForm> list = new ArrayList<>();

        for (DeviceVo deviceVo : waterFactoryList) {
            for (SecurityEnum securityEnum : SecurityEnum.values()) {
                SecuritySwitchForm securitySwitchForm = new SecuritySwitchForm();
                securitySwitchForm.setId(UuidUtils.getUuid());
                securitySwitchForm.setUserBy("system");
                securitySwitchForm.setCurr(new Date());
                securitySwitchForm.setStatus(true);
                securitySwitchForm.setFacilityId(deviceVo.getDeviceId());
                securitySwitchForm.setAddress(securityEnum);
                securitySwitchForm.setEnterpriseId(enterpriseId);
                securitySwitchForm.setPond(securityEnum.getParent());
                list.add(securitySwitchForm);
            }
        }
        this.iSecurityService.adds(list);
    }

    @Override
    public Pagination<SecurityLogVo> logList(SecurityLogForm securityLogForm) throws FrameworkRuntimeException {
        String enterpriseId = iAuthCasWeb.get().getEnterpriseId();
        securityLogForm.setEnterpriseId(enterpriseId);
        Pagination<SecurityLogVo> list = iSecurityService.logList(securityLogForm);
        return list;
    }

    @Override
    public String updateTask(String enterpriseId) throws FrameworkRuntimeException {

        Random random = new Random();
        SecurityEnum[] values = SecurityEnum.values();
        double d = random.nextDouble() * values.length;
        // 随机取一个地点
        SecurityLogForm securityLogForm = new SecurityLogForm();
        securityLogForm.setId(UuidUtils.getUuid());
        securityLogForm.setAddress(values[(int) Math.round(d)]);
        securityLogForm.setPond(securityLogForm.getAddress().getParent());
        securityLogForm.setEnterpriseId(enterpriseId);
        securityLogForm.setCurr(new Date());
        securityLogForm.setUserBy("system");
        int status = (int) Math.round(random.nextDouble());
        securityLogForm.setStatus(String.valueOf(status));
        this.iSecurityService.addLog(securityLogForm);
        return null;
    }


}