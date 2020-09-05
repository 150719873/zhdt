package com.dotop.smartwater.project.module.api.device.impl;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.api.device.IDeviceSubscribeFactory;
import com.dotop.smartwater.project.module.core.water.bo.DeviceSubscribeBo;
import com.dotop.smartwater.project.module.core.water.constants.DeviceCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceSubscribeForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceSubscribeVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.service.device.IDeviceService;
import com.dotop.smartwater.project.module.service.device.IDeviceSubscribeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class DeviceSubscribeFactoryImpl implements IDeviceSubscribeFactory {

    private final static Logger logger = LogManager.getLogger(DeviceSubscribeFactoryImpl.class);

    @Autowired
    private IDeviceSubscribeService iDeviceSubscribeService;

    @Autowired
    private IDeviceService iDeviceService;

    @Override
    public DeviceSubscribeVo add(DeviceSubscribeForm form) {
        form.setSubscribeTime(new Date());
        DeviceSubscribeBo bo = BeanUtils.copy(form, DeviceSubscribeBo.class);
        DeviceSubscribeVo vo = iDeviceSubscribeService.get(bo);
        if (vo != null) {
            throw new FrameworkRuntimeException(DeviceCode.DEVICE_SUBSCRIBE, "该水表已订阅");
        }
        DeviceVo deviceVo = iDeviceService.findByDevNo(form.getDevno());
        if (deviceVo == null) {
            throw new FrameworkRuntimeException(DeviceCode.DEVICE_NOT_EXIST, "水务系统没有该水表");
        }
        if (!form.getEnterpriseid().equals(deviceVo.getEnterpriseid())) {
            throw new FrameworkRuntimeException(DeviceCode.DEVICE_MISMATCH, "输入的水司ID和水务系统绑定的不匹配");
        }
        iDeviceSubscribeService.add(bo);
        return null;
    }

    @Override
    public String del(DeviceSubscribeForm form) {
        DeviceSubscribeBo bo = BeanUtils.copy(form, DeviceSubscribeBo.class);
        DeviceSubscribeVo vo = iDeviceSubscribeService.get(bo);
        if (vo == null) {
            return null;
        }
        DeviceVo deviceVo = iDeviceService.findByDevNo(form.getDevno());
        if (deviceVo == null) {
            throw new FrameworkRuntimeException(DeviceCode.DEVICE_NOT_EXIST, "水务系统没有该水表");
        }
        if (!vo.getEnterpriseid().equals(deviceVo.getEnterpriseid())) {
            throw new FrameworkRuntimeException(DeviceCode.DEVICE_MISMATCH, "输入的水司ID和水务系统绑定的不匹配");
        }
        iDeviceSubscribeService.del(bo);
        return null;
    }

}
