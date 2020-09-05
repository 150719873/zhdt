package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDeviceFactory;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDeviceUplinkFactory;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDockingFactory;
import com.dotop.smartwater.project.third.module.api.factory.kbl.IKblDeviceFactory;
import com.dotop.smartwater.project.third.module.api.service.IMeterDeviceService;
import com.dotop.smartwater.project.third.module.api.service.IWaterDeviceService;
import com.dotop.smartwater.project.third.module.client.IWaterClientService;
import com.dotop.smartwater.project.third.module.core.utils.SaltUtils;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.third.module.core.water.bo.DockingBo;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
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
@Component("KblDeviceFactoryImpl")
public class KblDeviceFactoryImpl implements IKblDeviceFactory {

    private static final Logger LOGGER = LogManager.getLogger(KblDeviceFactoryImpl.class);

    @Autowired
    IMeterDeviceFactory iMeterDeviceFactory;
    @Autowired
    IWaterDeviceService iWaterDeviceService;
    @Autowired
    IMeterDeviceService iMeterDeviceService;
    @Autowired
    IMeterDeviceUplinkFactory iMeterDeviceUplinkFactory;
    @Autowired
    IWaterClientService iWaterClientService;
    @Autowired
    IMeterDockingFactory iMeterDockingFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void updateDevice(DockingForm loginDocking, DeviceForm combineDevice) throws FrameworkRuntimeException {
        try {
            LOGGER.info("开始更新设备信息-----------------------");
            String enterpriseid = combineDevice.getEnterpriseid();            // 查询中间库是否存在
            DeviceForm deviceForm = new DeviceForm();
            deviceForm.setEnterpriseid(enterpriseid);
            deviceForm.setDevno(combineDevice.getDevno());
            DeviceVo deviceVo = iMeterDeviceFactory.get(deviceForm);
            // 同步水表
            if (deviceVo == null) {
                // 中间库不存在
                deviceVo = iWaterDeviceService.get(BeanUtils.copy(combineDevice, DeviceBo.class));
                if (deviceVo != null) {
                    // 中间库不存在，但水务库存在
                    DeviceBo sync = BeanUtils.copy(deviceVo, DeviceBo.class);
                    sync.setId(UuidUtils.getUuid());
                    sync.setEnterpriseid(enterpriseid);
                    List<DeviceBo> deviceBos = new ArrayList<>();
                    deviceBos.add(sync);
                    iMeterDeviceService.adds(deviceBos);
                }
            }
            if (deviceVo == null) {
                // 中间库不存在，水务库不存在
                LOGGER.info(LogMsg.to("msg", "该水表不存在", "combineDevice", combineDevice));
                return;
//                throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "该水表不存在");
            }
            //新增的水表
            List<DeviceForm> addDeviceForms = new ArrayList<>();
            //中间库新增的水量list
            List<DeviceUplinkForm> addDeviceUplinkForms = new ArrayList<>();
            //中间库更新的水量list
            List<DeviceUplinkForm> updateDeviceUplinkForms = new ArrayList<>();
            //水务库新增的水量list
            List<DeviceUplinkForm> wAddDeviceUplinkForms = new ArrayList<>();
            if (deviceVo == null) {
                //新增水表
                combineDevice.setId(UuidUtils.getUuid());
                combineDevice.setDevid(UuidUtils.getUuid());
                deviceVo = BeanUtils.copy(combineDevice, DeviceVo.class);
                addDeviceForms.add(combineDevice);
                iMeterDeviceFactory.adds(addDeviceForms);
                //   登录信息
                iWaterClientService.deviceAdds(BeanUtils.copy(addDeviceForms, DeviceBo.class), BeanUtils.copy(loginDocking, DockingBo.class));
            }
            //更新读数
            DeviceUplinkForm search = new DeviceUplinkForm();
            search.setDevno(combineDevice.getDevno());
            search.setEnterpriseid(enterpriseid);
            List<DeviceUplinkVo> deviceUplinkVos = iMeterDeviceUplinkFactory.list(search);
            List<DeviceUplinkForm> deviceUplinkForms = combineDevice.getDeviceUplinkForms();
            // 上报只是一个设备
            DeviceUplinkForm deviceUplinkForm = deviceUplinkForms.get(0);
            if (deviceUplinkVos.isEmpty()) {
                //新增抄表记录
                deviceUplinkForm.setId(UuidUtils.getUuid());
                deviceUplinkForm.setDevno(deviceVo.getDevno());
                deviceUplinkForm.setDevid(deviceVo.getDevid());
                deviceUplinkForm.setDeveui(deviceVo.getDeveui());
                deviceUplinkForm.setTapstatus(1);
                deviceUplinkForm.setIccid(combineDevice.getIccid());
//                deviceUplinkForm.setUplinkData( );
//                deviceUplinkForm.setRssi();
//                deviceUplinkForm.setLsnr();
                deviceUplinkForm.setEnterpriseid(deviceVo.getEnterpriseid());
                addDeviceUplinkForms.add(deviceUplinkForm);
                wAddDeviceUplinkForms.add(deviceUplinkForm);
            } else if (deviceUplinkVos.size() == 1) {
                DeviceUplinkVo deviceUplinkVo = deviceUplinkVos.get(0);
                // 如果10分钟之内的上报均跳过更新
                LOGGER.info(LogMsg.to("msg", "如果10分钟之内的上报均跳过更新", "seconds", DateUtils.secondsBetween(deviceUplinkForm.getUplinkDate(), deviceUplinkVo.getUplinkDate())));
                if (Math.abs(DateUtils.secondsBetween(deviceUplinkForm.getUplinkDate(), deviceUplinkVo.getUplinkDate())) > 300) {
                    String saltStr = SaltUtils.getRecordSalt(deviceUplinkForm.getUplinkDate(), "", deviceUplinkForm.getWater(), deviceUplinkForm.getDevno(), "");
                    String saltStr2 = SaltUtils.getRecordSalt(deviceUplinkVo.getUplinkDate(), "", deviceUplinkVo.getWater(), deviceUplinkVo.getDevno(), "");
                    if (!saltStr.equals(saltStr2)) {
                        deviceUplinkVo.setUplinkDate(deviceUplinkForm.getUplinkDate());
                        deviceUplinkVo.setWater(deviceUplinkForm.getWater());
                        deviceUplinkVo.setTapstatus(1);
                        deviceUplinkVo.setIccid(combineDevice.getIccid());
                        deviceUplinkVo.setUplinkData(deviceUplinkForm.getUplinkData());
                        deviceUplinkVo.setRssi(deviceUplinkForm.getRssi());
                        deviceUplinkVo.setLsnr(deviceUplinkForm.getLsnr());
                        updateDeviceUplinkForms.add(BeanUtils.copy(deviceUplinkVo, DeviceUplinkForm.class));
                        wAddDeviceUplinkForms.add(BeanUtils.copy(deviceUplinkVo, DeviceUplinkForm.class));
                    }
                }

            } else {
                throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "数据库数据异常");
            }
            if (!addDeviceUplinkForms.isEmpty()) {
                iMeterDeviceUplinkFactory.adds(addDeviceUplinkForms);
            }
            if (!updateDeviceUplinkForms.isEmpty()) {
                iMeterDeviceUplinkFactory.edits(updateDeviceUplinkForms);
            }
            if (!wAddDeviceUplinkForms.isEmpty()) {
                iWaterClientService.uplinks(BeanUtils.copy(wAddDeviceUplinkForms, DeviceUplinkBo.class), BeanUtils.copy(loginDocking, DockingBo.class));
            }
            LOGGER.info("更新设备信息结束-----------------------");
        } catch (Exception e) {
            LOGGER.error("kbl更新设备信息发生错误");
            LOGGER.error(LogMsg.to("e", e, "combineDevice:", combineDevice));
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, e.getMessage(), e);
        }
    }
}
