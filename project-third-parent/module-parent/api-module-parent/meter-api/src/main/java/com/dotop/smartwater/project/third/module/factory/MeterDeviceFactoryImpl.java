package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDeviceFactory;
import com.dotop.smartwater.project.third.module.api.service.IMeterDeviceService;
import com.dotop.smartwater.project.third.module.api.service.IWaterDeviceService;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Component
public class MeterDeviceFactoryImpl implements IMeterDeviceFactory {

    private final static Logger LOGGER = LogManager.getLogger(MeterDeviceFactoryImpl.class);

    @Autowired
    private IMeterDeviceService iMeterDeviceService;
    @Autowired
    private IWaterDeviceService iWaterDeviceService;

    @Override
    public List<DeviceVo> list(DeviceForm deviceForm) throws FrameworkRuntimeException {
        DeviceBo deviceBo = BeanUtils.copy(deviceForm, DeviceBo.class);
        return iMeterDeviceService.list(deviceBo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void adds(List<DeviceForm> deviceForms) throws FrameworkRuntimeException {
        List<DeviceBo> deviceBos = BeanUtils.copy(deviceForms, DeviceBo.class);
        iMeterDeviceService.adds(deviceBos);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void edits(List<DeviceForm> deviceForms) throws FrameworkRuntimeException {
        List<DeviceBo> deviceBos = BeanUtils.copy(deviceForms, DeviceBo.class);
        iMeterDeviceService.edits(deviceBos);
    }

    @Override
    public DeviceVo get(DeviceForm deviceForm) throws FrameworkRuntimeException {
        DeviceBo deviceBo = BeanUtils.copy(deviceForm, DeviceBo.class);
        return iMeterDeviceService.get(deviceBo);
    }

    /**
     * 校验devno唯一，需要同步后才能使用
     * @param deviceForms
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public List<DeviceForm> check(List<DeviceForm> deviceForms, DockingForm loginDocking) throws FrameworkRuntimeException {
        DeviceBo search = new DeviceBo();
        // search.setEnterpriseid(loginDocking.getEnterpriseid());//注释后，表示，全局筛选
        List<DeviceVo> list = iWaterDeviceService.list(search);
        Map<String, DeviceVo> collect = list.stream().collect(Collectors.toMap(DeviceVo::getDevno, s -> s));
        //重复的list，需要日志记录错误
        List<DeviceForm> repeat = new ArrayList<>();
        //不重复的list
        List<DeviceForm> noRepeat = new ArrayList<>();
        Set<String> hashSet = new HashSet<>();
        deviceForms.forEach(deviceForm -> {
            DeviceVo check = collect.get(deviceForm.getDevno());
            //系统中不存在
            if (check != null) {
                //如果deveui也相同
                if (deviceForm.getDeveui().equals(check.getDeveui()) && hashSet.add(deviceForm.getDevno())) {
                    noRepeat.add(deviceForm);
                } else {
                    repeat.add(deviceForm);
                }
                //该else if用于判断deviceForms是否自身重复
            } else if (hashSet.add(deviceForm.getDevno())){
                noRepeat.add(deviceForm);
            } else {
                repeat.add(deviceForm);
            }
        });
        if (!repeat.isEmpty()) {
            LOGGER.error(LogMsg.to("deviceForms,重复的数设备", repeat));
        }
        return noRepeat;
    }
}
