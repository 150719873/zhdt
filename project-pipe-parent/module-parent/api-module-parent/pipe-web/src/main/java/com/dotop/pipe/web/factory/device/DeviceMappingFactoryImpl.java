package com.dotop.pipe.web.factory.device;

import com.dotop.pipe.web.api.factory.device.IDeviceMappingFactory;
import com.dotop.pipe.api.service.device.IDeviceMappingService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.device.DeviceMappingBo;
import com.dotop.pipe.core.form.DeviceMappingForm;
import com.dotop.pipe.core.vo.device.DeviceMappingVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *
 * @date 2019年2月15日
 */
@Component
public class DeviceMappingFactoryImpl implements IDeviceMappingFactory {

    private static final Logger logger = LogManager.getLogger(DeviceMappingFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IDeviceMappingService iDeviceMappingService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public DeviceMappingVo add(DeviceMappingForm deviceMappingForm) {

        LoginCas loginCas = iAuthCasApi.get();
        String enterpriseId = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        /*DeviceMappingBo deviceMappingBo = BeanUtils.copyProperties(deviceMappingForm,DeviceMappingBo.class);
         */

        // 处理数据
        DeviceMappingBo deviceMappingBo = new DeviceMappingBo();
        deviceMappingBo.setLevel(deviceMappingForm.getLevel());
        deviceMappingBo.setDeviceId(deviceMappingForm.getDeviceId());
        deviceMappingBo.setEnterpriseId(enterpriseId);
        deviceMappingBo.setUserBy(userBy);
        deviceMappingBo.setCurr(new Date());

        // 删除
        if (deviceMappingForm.getDelMapIds() != null && deviceMappingForm.getDelMapIds().size() > 0) {
            deviceMappingBo.setDelMapIds(deviceMappingForm.getDelMapIds());
            iDeviceMappingService.delMapping(deviceMappingBo);
            deviceMappingBo.setDelMapIds(null);
        }

        // 正向新增
        if (deviceMappingForm.getAddPositiveOtherIds() != null
                && deviceMappingForm.getAddPositiveOtherIds().size() > 0) {  // 正向
            deviceMappingBo.setDirection("1");
            deviceMappingBo.setAddPositiveOtherIds(deviceMappingForm.getAddPositiveOtherIds());
            iDeviceMappingService.addMapping(deviceMappingBo);
            deviceMappingBo.setAddPositiveOtherIds(null);
            deviceMappingBo.setDirection(null);
        }

        // 反向新增
        if (deviceMappingForm.getAddReverseOtherIds() != null
                && deviceMappingForm.getAddReverseOtherIds().size() > 0) { // 反向
            deviceMappingBo.setDirection("0");
            deviceMappingBo.setAddReverseOtherIds(deviceMappingForm.getAddReverseOtherIds());
            iDeviceMappingService.addMapping(deviceMappingBo);
            deviceMappingBo.setAddReverseOtherIds(null);
            deviceMappingBo.setDirection(null);
        }
        return null;
    }

    @Override
    public List<DeviceMappingVo> mappingList(DeviceMappingForm deviceMappingForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String enterpriseId = loginCas.getOperEid();
        DeviceMappingBo deviceMappingBo = BeanUtils.copyProperties(deviceMappingForm, DeviceMappingBo.class);
        deviceMappingBo.setEnterpriseId(enterpriseId);
        List<DeviceMappingVo> list = iDeviceMappingService.mappingList(deviceMappingBo);
        return list;
    }

    @Override
    public List<DeviceMappingVo> regionMappingList(DeviceMappingForm deviceMappingForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String enterpriseId = loginCas.getOperEid();
        DeviceMappingBo deviceMappingBo = BeanUtils.copyProperties(deviceMappingForm, DeviceMappingBo.class);
        deviceMappingBo.setEnterpriseId(enterpriseId);
        List<DeviceMappingVo> list = iDeviceMappingService.regionMappingList(deviceMappingBo);
        return list;
    }
}
