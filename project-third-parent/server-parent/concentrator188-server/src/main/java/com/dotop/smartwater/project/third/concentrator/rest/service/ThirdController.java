package com.dotop.smartwater.project.third.concentrator.rest.service;

import com.dotop.smartwater.project.third.concentrator.api.IConcentratorDeviceFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.third.vo.concentrator.ConcentratorDeviceVo;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.third.concentrator.core.form.ConcentratorDeviceForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @date 2019-07-11
 */
@RestController
@RequestMapping("/third")
public class ThirdController implements BaseController<DeviceForm> {

    @Autowired
    IConcentratorDeviceFactory iConcentratorDeviceFactory;

    /**
     * 获取设备信息：包含集中器，采集器
     *
     * @param deviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    @PostMapping(value = "/device/get", produces = GlobalContext.PRODUCES)
    public String get(@RequestBody DeviceForm deviceForm) throws FrameworkRuntimeException {
        ConcentratorDeviceForm concentratorDeviceForm = BeanUtils.copy(deviceForm, ConcentratorDeviceForm.class);
        com.dotop.smartwater.project.third.concentrator.core.vo.ConcentratorDeviceVo concentratorDeviceVo = iConcentratorDeviceFactory.get(concentratorDeviceForm);
        ConcentratorDeviceVo result = new ConcentratorDeviceVo();
        result.setCollectorCode(concentratorDeviceVo.getCollector().getCode());
        result.setConcentratorCode(concentratorDeviceVo.getConcentrator().getCode());
        result.setCollectorId(concentratorDeviceVo.getCollector().getId());
        result.setConcentratorId(concentratorDeviceVo.getConcentrator().getId());
        return resp(result);
    }

    /**
     * 删除水表信息
     *
     * @param deviceForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/device/del", produces = GlobalContext.PRODUCES)
    public void delete(@RequestBody DeviceForm deviceForm) throws FrameworkRuntimeException {
        ConcentratorDeviceForm concentratorDeviceForm = BeanUtils.copy(deviceForm, ConcentratorDeviceForm.class);
        iConcentratorDeviceFactory.delete(concentratorDeviceForm);
    }
}
