package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDeviceUplinkFactory;
import com.dotop.smartwater.project.third.module.api.service.IMeterDeviceUplinkService;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceUplinkVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Component
public class MeterDeviceUplinkFactoryImpl implements IMeterDeviceUplinkFactory {
    private final static Logger LOGGER = LogManager.getLogger(MeterDeviceUplinkFactoryImpl.class);

    @Autowired
    private IMeterDeviceUplinkService iMeterDeviceUplinkService;

    @Override
    public List<DeviceUplinkVo> list(DeviceUplinkForm deviceUplinkForm) throws FrameworkRuntimeException {
        DeviceUplinkBo deviceUplinkBo = BeanUtils.copy(deviceUplinkForm, DeviceUplinkBo.class);
        return iMeterDeviceUplinkService.list(deviceUplinkBo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void adds(List<DeviceUplinkForm> deviceUplinkForms) throws FrameworkRuntimeException {
        List<DeviceUplinkBo> deviceUplinkBos = BeanUtils.copy(deviceUplinkForms, DeviceUplinkBo.class);
        iMeterDeviceUplinkService.adds(deviceUplinkBos);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void edits(List<DeviceUplinkForm> deviceUplinkForms) throws FrameworkRuntimeException {
        List<DeviceUplinkBo> deviceUplinkBos = BeanUtils.copy(deviceUplinkForms, DeviceUplinkBo.class);
        iMeterDeviceUplinkService.edits(deviceUplinkBos);
    }

    /**
     * 筛选抄表信息, 如果读数water为null, 不返回
     * @param deviceForms
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public List<DeviceUplinkForm> check(List<DeviceUplinkForm> deviceForms) throws FrameworkRuntimeException {
        List<DeviceUplinkForm> list = new ArrayList<>();
        List<DeviceUplinkForm> waterNulllist = new ArrayList<>();
        for (DeviceUplinkForm deviceForm : deviceForms) {
            if (deviceForm.getWater() != null) {
                list.add(deviceForm);
            } else {
                waterNulllist.add(deviceForm);
            }
        }
        if (!waterNulllist.isEmpty()) {
            LOGGER.error(LogMsg.to("读数water为null的集合", waterNulllist));
        }
        return list;
    }
}
