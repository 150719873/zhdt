package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDeviceFactory;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDeviceUplinkFactory;
import com.dotop.smartwater.project.third.module.api.factory.IMeterOwnerFactory;
import com.dotop.smartwater.project.third.module.api.factory.IThirdObtainFactory;
import com.dotop.smartwater.project.third.module.api.service.IMeterOwnerService;
import com.dotop.smartwater.project.third.module.api.service.IWaterDeviceService;
import com.dotop.smartwater.project.third.module.api.service.IWaterOwnerService;
import com.dotop.smartwater.project.third.module.api.service.IZhswService;
import com.dotop.smartwater.project.third.module.core.third.zhsw.form.ClientForm;
import com.dotop.smartwater.project.third.module.core.third.zhsw.form.MeterRecordForm;
import com.dotop.smartwater.project.third.module.core.utils.SaltUtils;
import com.dotop.smartwater.project.third.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.third.module.core.water.vo.OwnerVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Component("ZhswFactoryImpl")
public class ZhswFactoryImpl implements IThirdObtainFactory {

    private final static Logger LOGGER = LogManager.getLogger("zhsw");

    @Autowired
    private IWaterDeviceService iWaterDeviceService;
    @Autowired
    private IMeterDeviceFactory iMeterDeviceFactory;
    @Autowired
    private IMeterOwnerService iMeterOwnerService;
    @Autowired
    private IMeterOwnerFactory iMeterOwnerFactory;
    @Autowired
    private IWaterOwnerService iWaterOwnerService;
    @Autowired
    private IMeterDeviceUplinkFactory iMeterDeviceUplinkFactory;
    @Autowired
    private IZhswService iZhswService;



    @Override
    public void updateOwner(DockingForm dockingForm, DockingForm loginDocking) throws FrameworkRuntimeException {
        try {
            LOGGER.info("全景：上传客户信息开始--------------");
            DeviceForm deviceForm = new DeviceForm();
            deviceForm.setEnterpriseid(dockingForm.getEnterpriseid());
            List<DeviceVo> allMidDeviceList = iMeterDeviceFactory.list(deviceForm);
            Map<String, DeviceVo> midDeviceMap = allMidDeviceList.stream().collect(Collectors.toMap(DeviceVo::getDevno, s -> s));
            // 查找中间库中对应的enterpriseid的所有的device_uplink
            DeviceUplinkForm deviceUplinkForm = new DeviceUplinkForm();
            deviceUplinkForm.setEnterpriseid(dockingForm.getEnterpriseid());
            List<DeviceUplinkVo> midDeviceUplinkList = iMeterDeviceUplinkFactory.list(deviceUplinkForm);
            Map<String, DeviceUplinkVo> midDeviceUplinkMap = midDeviceUplinkList.stream().collect(Collectors.toMap(DeviceUplinkVo::getDevno, s -> s));
            // 查询水务库中所有的客户信息
            OwnerBo ownerBo = new OwnerBo();
            ownerBo.setEnterpriseid(dockingForm.getEnterpriseid());
            List<OwnerVo> waterOwnerList = iWaterOwnerService.list(ownerBo);
            List<DeviceVo> waterDeviceList = waterOwnerList.stream().map(o -> o.getDevice()).collect(Collectors.toList());
            // 水务库中所有的设备集合转换成map
            Map<String, DeviceVo> waterDeviceMap = waterDeviceList.stream().collect(Collectors.toMap(DeviceVo::getDevno, s -> s));
            List<DeviceForm> addMidlist = new ArrayList<>();
            List<ClientForm> addZhswList = new ArrayList<>();
            List<DeviceUplinkForm> addMidUplinklist = new ArrayList<>();
            waterDeviceMap.forEach((s, deviceVo) -> {
                if (midDeviceMap.get(s) == null) {
                    deviceVo.setId(UuidUtils.getUuid());
                    deviceVo.setEnterpriseid(dockingForm.getEnterpriseid());
                    DeviceForm copy = BeanUtils.copy(deviceVo, DeviceForm.class);
                    addMidlist.add(copy);
                    ClientForm clientForm = new ClientForm();
                    clientForm.setWcode(deviceVo.getDevno());
                    clientForm.setInstall_address(deviceVo.getDevaddr());
                    addZhswList.add(clientForm);
                }
            });
            waterDeviceMap.forEach((s, deviceVo) -> {
                if (deviceVo.getWater() != null) {
                    if (midDeviceUplinkMap.get(s) == null) {
                        DeviceUplinkForm deviceUplinkFormTemp = new DeviceUplinkForm();
                        deviceUplinkFormTemp.setWater(deviceVo.getWater());
                        deviceUplinkFormTemp.setUplinkDate(deviceVo.getUplinktime());
                        deviceUplinkFormTemp.setDevid(deviceVo.getDevid());
                        deviceUplinkFormTemp.setDevno(deviceVo.getDevno());
                        deviceUplinkFormTemp.setEnterpriseid(dockingForm.getEnterpriseid());
                        deviceUplinkFormTemp.setId(UuidUtils.getUuid());
                        addMidUplinklist.add(deviceUplinkFormTemp);
                    }
                }
            });

            if (!addMidlist.isEmpty()) {
                iMeterDeviceFactory.adds(addMidlist);
            }

            if (!addZhswList.isEmpty()) {
                iZhswService.addData(dockingForm, loginDocking, addZhswList);
            }

            if (!addMidUplinklist.isEmpty()) {
                iMeterDeviceUplinkFactory.adds(addMidUplinklist);
            }
            LOGGER.info("全景：上传客户信息完成--------------");
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }

    }

    @Override
    public void updateDevice(DockingForm dockingForm, DockingForm loginDocking) throws FrameworkRuntimeException {
        try{
            LOGGER.info("全景：更新水表信息开始--------------");
            // 查找中间库中对应的enterpriseid的所有的device_uplink
            DeviceUplinkForm deviceUplinkForm = new DeviceUplinkForm();
            deviceUplinkForm.setEnterpriseid(dockingForm.getEnterpriseid());
            List<DeviceUplinkVo> midDeviceUplinkList = iMeterDeviceUplinkFactory.list(deviceUplinkForm);
            Map<String, DeviceUplinkVo> midDeviceUplinkMap = midDeviceUplinkList.stream().collect(Collectors.toMap(DeviceUplinkVo::getDevno, s -> s));

            OwnerBo ownerBo = new OwnerBo();
            ownerBo.setEnterpriseid(dockingForm.getEnterpriseid());
            List<OwnerVo> waterOwnerList = iWaterOwnerService.list(ownerBo);
            List<DeviceVo> waterDeviceList = waterOwnerList.stream().map(o -> o.getDevice()).collect(Collectors.toList());
            // 水务库中所有的设备集合转换成map
            Map<String, DeviceVo> waterDeviceMap = waterDeviceList.stream().collect(Collectors.toMap(DeviceVo::getDevno, s -> s));

            List<DeviceUplinkForm> updateMidList = new ArrayList<>();
            List<MeterRecordForm> updateZhswList = new ArrayList<>();
            waterDeviceMap.forEach((s, deviceVo) -> {
                if (deviceVo.getWater() != null) {
                    if (midDeviceUplinkMap.get(s) != null) {
                        String deviceRecordSalt = SaltUtils.getRecordSalt(deviceVo.getUplinktime(), "", deviceVo.getWater(), "", deviceVo.getDevid());
                        DeviceUplinkVo deviceUplinkVo = midDeviceUplinkMap.get(s);
                        String midRecordSalt = SaltUtils.getRecordSalt(deviceUplinkVo.getUplinkDate(), "", deviceUplinkVo.getWater(), "", deviceUplinkVo.getDevid());
                        if (!deviceRecordSalt.equals(midRecordSalt)) {
                            DeviceUplinkForm deviceUplinkFormTemp = new DeviceUplinkForm();
                            deviceUplinkFormTemp.setWater(deviceVo.getWater());
                            deviceUplinkFormTemp.setUplinkDate(deviceVo.getUplinktime());
                            deviceUplinkFormTemp.setEnterpriseid(dockingForm.getEnterpriseid());
                            deviceUplinkFormTemp.setDevno(deviceUplinkVo.getDevno());
                            deviceUplinkFormTemp.setId(deviceUplinkVo.getId());
                            updateMidList.add(deviceUplinkFormTemp);
                            MeterRecordForm meterRecordForm = new MeterRecordForm();
                            meterRecordForm.setWcode(deviceUplinkVo.getDevno());
                            int time = (int)deviceVo.getUplinktime().getTime();
                            meterRecordForm.setCurrent_time(time);
                            BigDecimal bigDecimal = new BigDecimal(deviceVo.getWater());
                            meterRecordForm.setCurrent_num(bigDecimal);
                            updateZhswList.add(meterRecordForm);
                        }


                    }
                }
            });
            if (!updateMidList.isEmpty()) {
                iMeterDeviceUplinkFactory.edits(updateMidList);
            }
            if (!updateZhswList.isEmpty()) {
                iZhswService.updateData(dockingForm, loginDocking, updateZhswList);
            }
            LOGGER.info("全景：更新水表信息完成--------------");
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
