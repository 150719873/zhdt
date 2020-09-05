package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.project.third.module.core.water.bo.*;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.constants.ModeConstants;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDeviceFactory;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDeviceUplinkFactory;
import com.dotop.smartwater.project.third.module.api.factory.IMeterOwnerFactory;
import com.dotop.smartwater.project.third.module.api.factory.IThirdObtainFactory;
import com.dotop.smartwater.project.third.module.api.service.IMeterOwnerService;
import com.dotop.smartwater.project.third.module.api.service.IWaterDeviceService;
import com.dotop.smartwater.project.third.module.api.service.IWaterOwnerService;
import com.dotop.smartwater.project.third.module.client.IWaterClientService;
import com.dotop.smartwater.project.third.module.core.utils.SaltUtils;
import com.dotop.smartwater.project.third.module.core.utils.VideoUtils;
import com.dotop.smartwater.project.third.module.core.water.bo.*;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.third.module.core.water.vo.OwnerVo;
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
@Component("VideoFactoryImpl")
public class VideoFactoryImpl implements IThirdObtainFactory {

    private final static Logger LOGGER = LogManager.getLogger("video");

    @Autowired
    private IMeterOwnerFactory iMeterOwnerFactory;

    @Autowired
    private IMeterDeviceFactory iMeterDeviceFactory;

    @Autowired
    private IMeterOwnerService iMeterOwnerService;

    @Autowired
    private IWaterDeviceService iWaterDeviceService;

    @Autowired
    private IMeterDeviceUplinkFactory iMeterDeviceUplinkFactory;

    @Autowired
    private IWaterOwnerService iWaterOwnerService;

    @Autowired
    private IWaterClientService iWaterClientService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void updateOwner(DockingForm dockingForm, DockingForm loginDockingForm) throws FrameworkRuntimeException {
        try {
            LOGGER.info("视频直读：开始更新客户信息--------------");
            // 获取第一页客户的信息
            Pagination<OwnerForm> customerPage = VideoUtils.getCustomer(dockingForm, loginDockingForm, 1);
            List<OwnerForm> ownerVoList = customerPage.getData();
            // 客户列表总数量
            long total = customerPage.getTotalPageSize();
            // 获取pagesize
            long pageSize = customerPage.getPageSize();
            //总页数
            long totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
            // 查询中间库中所有的客户信息
            OwnerForm ownerForm = new OwnerForm();
            ownerForm.setEnterpriseid(dockingForm.getEnterpriseid());
            List<OwnerVo> allMidOwnerList = iMeterOwnerFactory.list(ownerForm);
            Map<String, OwnerVo> midCustomerMap = allMidOwnerList.stream().collect(Collectors.toMap(OwnerVo::getUserno, s -> s));
            // 查询水务库中所有的客户信息
            OwnerBo ownerBo = new OwnerBo();
            ownerBo.setEnterpriseid(dockingForm.getEnterpriseid());
            List<OwnerVo> allWaterOwnerList = iWaterOwnerService.list(ownerBo);
            Map<String, OwnerVo> waterCustomerMap = allWaterOwnerList.stream().collect(Collectors.toMap(OwnerVo::getUserno, s -> s));
            // 查询中间库中所有的device
            DeviceForm deviceForm = new DeviceForm();
            deviceForm.setEnterpriseid(dockingForm.getEnterpriseid());
            List<DeviceVo> allMidDeviceList = iMeterDeviceFactory.list(deviceForm);
            Map<String, DeviceVo> midDeviceMap = allMidDeviceList.stream().collect(Collectors.toMap(DeviceVo::getDevno, s -> s));


            List<OwnerForm> updateWater2Midlist = new ArrayList<>();
            waterCustomerMap.forEach((s, customerVo) -> {
                if (midCustomerMap.get(s) == null) {
                    customerVo.setId(UuidUtils.getUuid());
                    OwnerForm copy = BeanUtils.copy(customerVo, OwnerForm.class);
                    updateWater2Midlist.add(copy);
                    midCustomerMap.put(s, customerVo);
                    allMidOwnerList.add(customerVo);

                }
            });
            if (!updateWater2Midlist.isEmpty()) {
                iMeterOwnerFactory.adds(updateWater2Midlist);
            }
            // 筛查出需要新增或者更新到中间库的客户信息
            for (int i = 1; i <= totalPage; i++) {
                try {
                    // 需要新增到中间库的list
                    List<OwnerForm> addMidlist = new ArrayList<>();
                    // 需要更新到中间库的list
                    List<OwnerForm> updateMidlist = new ArrayList<>();

                    // 需要新增到水务库的list
                    List<OwnerForm> addWaterlist = new ArrayList<>();
                    // 需要更新到水务库的list
                    List<OwnerForm> updateWaterlist = new ArrayList<>();
                    // 水务库的换表list
                    List<OwnerChangeBo> changeMeterlist = new ArrayList<>();

                    if (i > 1) {
                        ownerVoList = VideoUtils.getCustomer(dockingForm, loginDockingForm, i).getData();
                    }
                    ownerVoList = iMeterOwnerFactory.check(ownerVoList);
                    // 将第三方用户list转换成Map
                    Map<String, OwnerForm> ownerVoMap = ownerVoList.stream().collect(Collectors.toMap(OwnerForm::getUserno, s -> s));

                    ownerVoMap.forEach((s, customerVo) -> {
                        if (midCustomerMap.get(s) == null) {
                            if (customerVo.getDevice() != null && customerVo.getDevice().getDevno() != null) {
                                DeviceVo deviceVo = midDeviceMap.get(customerVo.getDevice().getDevno());
                                if (deviceVo != null) {
                                    customerVo.setDevid(deviceVo.getDevid());

                                    OwnerChangeBo ownerChangeBo = new OwnerChangeBo();
                                    ownerChangeBo.setOwnerid(customerVo.getOwnerid());
                                    ownerChangeBo.setDevid(deviceVo.getDevid());
                                    ownerChangeBo.setOldDevid(deviceVo.getDevid());
                                    changeMeterlist.add(ownerChangeBo);

                                }
                            }
                            addMidlist.add(customerVo);
                            addWaterlist.add(customerVo);

                        } else {
                            if (waterCustomerMap.get(s) == null) {
                                addWaterlist.add(customerVo);
                            }
                            String meterIdStr = "";
                            if (customerVo.getDevice() != null && customerVo.getDevice().getDevno() != null) {
                                DeviceVo deviceVo = midDeviceMap.get(customerVo.getDevice().getDevno());
                                if (deviceVo != null) {
                                    meterIdStr = deviceVo.getDevid();
                                }
                            }
                            String customerSaltStr = SaltUtils.getOwnerSalt(customerVo.getUserno(), customerVo.getUsername(), customerVo.getUseraddr(), customerVo.getJson(), meterIdStr);
                            OwnerVo ownerVo = midCustomerMap.get(s);
                            String deviceIdStr = "";
                            if (ownerVo.getDevid() != null) {
                                deviceIdStr = ownerVo.getDevid();
                            }
                            String ownerSaltStr = SaltUtils.getOwnerSalt(ownerVo.getUserno(), ownerVo.getUsername(), ownerVo.getUseraddr(), ownerVo.getJson(), deviceIdStr);
                            if (!customerSaltStr.equals(ownerSaltStr)) {
                                if (deviceIdStr.length() == 0) {
                                    OwnerChangeBo ownerChangeBo = new OwnerChangeBo();
                                    ownerChangeBo.setOwnerid(ownerVo.getOwnerid());
                                    ownerChangeBo.setDevid(meterIdStr);
                                    ownerChangeBo.setOldDevid(meterIdStr);
                                    changeMeterlist.add(ownerChangeBo);
                                } else if (!deviceIdStr.equals(meterIdStr)) {
                                    OwnerChangeBo ownerChangeBo = new OwnerChangeBo();
                                    ownerChangeBo.setOwnerid(ownerVo.getOwnerid());
                                    ownerChangeBo.setDevid(meterIdStr);
                                    ownerChangeBo.setOldDevid(deviceIdStr);
                                    changeMeterlist.add(ownerChangeBo);
                                }
                                OwnerForm copy = BeanUtils.copy(ownerVo, OwnerForm.class);
                                copy.setDevid(meterIdStr);
                                copy.setUserno(customerVo.getUserno());
                                copy.setUsername(customerVo.getUsername());
                                copy.setUseraddr(customerVo.getUseraddr());
                                copy.setJson(customerVo.getJson());
                                updateMidlist.add(copy);
                                if (waterCustomerMap.get(s) != null) {
                                    updateWaterlist.add(copy);
                                }
                            }
                        }
                    });

                    // 批量新增中间库的客户信息(addMidlist)
                    if (!addMidlist.isEmpty()) {
                        iMeterOwnerFactory.adds(addMidlist);
                    }
                    // 批量修改中间库的客户信息(updateMidlist)
                    if (!updateMidlist.isEmpty()) {
                        iMeterOwnerFactory.edits(updateMidlist, dockingForm.getEnterpriseid());
                    }
                    // TODO 批量新增水务库的客户信息(addWaterlist)
                    if (!addWaterlist.isEmpty()) {
                        iWaterClientService.ownerAdds(BeanUtils.copy(addWaterlist, OwnerBo.class), BeanUtils.copy(loginDockingForm, DockingBo.class));
                    }
                    //  TODO 批量修改水务库的客户基本信息(updateWaterlist)
                    if (!updateWaterlist.isEmpty()) {
                        iWaterClientService.ownerEdits(BeanUtils.copy(updateWaterlist, OwnerBo.class), BeanUtils.copy(loginDockingForm, DockingBo.class));
                    }
                    //  TODO 批量修改水务库的换表的客户(changeMeterlist)
                    if (!changeMeterlist.isEmpty()) {
                        iWaterClientService.ownerChanges(changeMeterlist, BeanUtils.copy(loginDockingForm, DockingBo.class));
                    }
                } catch (Exception e) {
                    LOGGER.error(LogMsg.to(e));
                    throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
                }
            }


            LOGGER.info("视频直读：客户信息更新完成--------------");
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void updateDevice(DockingForm dockingForm, DockingForm loginDockingForm) throws FrameworkRuntimeException {

        LOGGER.info("视频直读：开始更新设备信息--------------");
        Pagination<DeviceForm> meterPage = VideoUtils.getMeter(dockingForm, loginDockingForm, 1);
        List<DeviceForm> meterVoList = meterPage.getData();
        long total = meterPage.getTotalPageSize();
        long pageSize = meterPage.getPageSize();
        //总页数
        long totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        // 查询中间库中所有的设备信息
        DeviceForm deviceForm = new DeviceForm();
        deviceForm.setEnterpriseid(dockingForm.getEnterpriseid());
        List<DeviceVo> allMidDeviceList = iMeterDeviceFactory.list(deviceForm);
        Map<String, DeviceVo> midDeviceMap = allMidDeviceList.stream().collect(Collectors.toMap(DeviceVo::getDeveui, s -> s));
        // 查询水务库中所有的设备信息
        DeviceBo deviceBo = new DeviceBo();
        deviceBo.setEnterpriseid(dockingForm.getEnterpriseid());
        deviceBo.setMode(loginDockingForm.getMode());
        List<DeviceVo> allWaterdeviceList = iWaterDeviceService.list(deviceBo);
        Map<String, DeviceVo> waterDeviceMap = allWaterdeviceList.stream().collect(Collectors.toMap(DeviceVo::getDeveui, s -> s));


        // 需要将水务库的设备同步新增到中间库的list
        List<DeviceForm> addWater2Midlist = new ArrayList<>();
        // 需要将水务库的设备同步修改到中间库的list
        List<DeviceForm> updateWater2Midlist = new ArrayList<>();
        waterDeviceMap.forEach((s, waterDeviceVo) -> {
            DeviceVo tempDeviceVo = midDeviceMap.get(s);
            if (tempDeviceVo == null) {
                waterDeviceVo.setId(UuidUtils.getUuid());
                DeviceForm copy = BeanUtils.copy(waterDeviceVo, DeviceForm.class);
                addWater2Midlist.add(copy);
                midDeviceMap.put(s, waterDeviceVo);
                allMidDeviceList.add(waterDeviceVo);
            } else if (!tempDeviceVo.getDevid().equals(waterDeviceVo.getDevid()) || !tempDeviceVo.getDevno().equals(waterDeviceVo.getDevno())) {
                String[] modes = waterDeviceVo.getMode().split(",");
                switch (modes[modes.length - 1]) {
                    case ModeConstants.SPZD:
                        tempDeviceVo.setDevid(waterDeviceVo.getDevid());
                        tempDeviceVo.setDevno(waterDeviceVo.getDevno());
                        updateWater2Midlist.add(BeanUtils.copy(tempDeviceVo, DeviceForm.class));
                        break;
                    default:
                        break;
                }
            }
        });
        try {
            if (!addWater2Midlist.isEmpty()) {
                iMeterDeviceFactory.adds(addWater2Midlist);
            }
            if (!updateWater2Midlist.isEmpty()) {
                iMeterDeviceFactory.edits(updateWater2Midlist);
            }

        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }

        for (int i = 1; i <= totalPage; i++) {
            try {
                // 需要新增到中间库的list
                List<DeviceForm> addMidlist = new ArrayList<>();
                // 需要更新到中间库的list
                List<DeviceForm> updateMidlist = new ArrayList<>();
                // 需要新增到水务库的list
                List<DeviceForm> addWaterlist = new ArrayList<>();
                // 需要更新到水务库的list
                List<DeviceForm> updateWaterlist = new ArrayList<>();
                if (i > 1) {
                    meterVoList = VideoUtils.getMeter(dockingForm, loginDockingForm, i).getData();
                }
                meterVoList = iMeterDeviceFactory.check(meterVoList, loginDockingForm);
                // 将第三方设备list转换成Map
                Map<String, DeviceForm> meterVoMap = meterVoList.stream().collect(Collectors.toMap(DeviceForm::getDeveui, s -> s));


                meterVoMap.forEach((s, meterVo) -> {
                    if (midDeviceMap.get(s) == null) {
                        addMidlist.add(meterVo);
                        addWaterlist.add(meterVo);
                    } else {
                        DeviceVo deviceVo = midDeviceMap.get(s);
                        if (waterDeviceMap.get(s) == null) {
                            addWaterlist.add(meterVo);
                        }
                        String meterSaltStr = SaltUtils.getDeviceSalt("", meterVo.getJson(), meterVo.getDeveui(), meterVo.getDevno());

                        String deviceSaltStr = SaltUtils.getDeviceSalt("", deviceVo.getJson(), deviceVo.getDeveui(), deviceVo.getDevno());
                        if (!deviceSaltStr.equals(meterSaltStr)) {
                            DeviceForm copy = BeanUtils.copy(deviceVo, DeviceForm.class);
                            copy.setJson(meterVo.getJson());
                            copy.setDevno(meterVo.getDevno());
                            copy.setDeveui(meterVo.getDeveui());
                            updateMidlist.add(copy);
                            if (waterDeviceMap.get(s) != null) {
                                updateWaterlist.add(copy);
                            }
                        }
                    }
                });
                // 批量新增中间库的设备信息(addMidlist)
                if (!addMidlist.isEmpty()) {
                    iMeterDeviceFactory.adds(addMidlist);
                }
                // 批量修改中间库的设备信息(updateMidlist)
                if (!updateMidlist.isEmpty()) {
                    iMeterDeviceFactory.edits(updateMidlist);
                }

                if (!addWaterlist.isEmpty()) {
                    iWaterClientService.deviceAdds(BeanUtils.copy(addWaterlist, DeviceBo.class), BeanUtils.copy(loginDockingForm, DockingBo.class));
                }

                if (!updateWaterlist.isEmpty()) {
                    iWaterClientService.deviceEdits(BeanUtils.copy(updateWaterlist, DeviceBo.class), BeanUtils.copy(loginDockingForm, DockingBo.class));
                }
            } catch (Exception e) {
                LOGGER.error(LogMsg.to(e));
                throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
            }
        }
        LOGGER.info("视频直读：设备信息更新完成--------------");

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void updateDeviceUplink(DockingForm dockingForm, DockingForm loginDockingForm) throws FrameworkRuntimeException {
        try {
            LOGGER.info("视频直读：开始更新抄表信息--------------");
            // 查询所有的抄表信息
            List<DeviceUplinkForm> allList = VideoUtils.getRecord(dockingForm, loginDockingForm);
            //先按照时间进行排列然后去重(一个设备只取最新的抄表数据)
            List<DeviceUplinkForm> sortList = allList.stream().sorted(Comparator.comparing(DeviceUplinkForm::getUplinkDate).reversed()).collect(Collectors.toList());
            // 对deveui和devno进行去重, 只有deveui和devno都相同时才认为是相同的设备的抄表读数,进行去重
            List<DeviceUplinkForm> recordVos = sortList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getDeveui() + ";" + o.getDevno()))), ArrayList::new));

            // 查询中间库中所有的抄表信息
            DeviceUplinkForm deviceUplinkForm = new DeviceUplinkForm();
            deviceUplinkForm.setEnterpriseid(dockingForm.getEnterpriseid());
            List<DeviceUplinkVo> midDeviceUplinkList = iMeterDeviceUplinkFactory.list(deviceUplinkForm);
            Map<String, DeviceUplinkVo> midDeviceUplinkMap = midDeviceUplinkList.stream().collect(Collectors.toMap(DeviceUplinkVo::getDeveui, s -> s));
            // 查询中间库中所有的device
            DeviceForm deviceForm = new DeviceForm();
            deviceForm.setEnterpriseid(dockingForm.getEnterpriseid());
            List<DeviceVo> allMidDeviceList = iMeterDeviceFactory.list(deviceForm);
            Map<String, DeviceVo> midDeviceMap = allMidDeviceList.stream().collect(Collectors.toMap(DeviceVo::getDeveui, s -> s));

            // 对去重后的数据再进行筛选, 与中间库的device表进行比较, 只有和device表的deveui和devno都相同的时候才要保存该条抄表数据, 否则不进行保存
            List<DeviceUplinkForm> finalRecordList = new ArrayList<>();
            for (DeviceVo deviceVo : allMidDeviceList) {
                for (DeviceUplinkForm recordVo : recordVos) {
                    if (deviceVo.getDeveui().equals(recordVo.getDeveui()) && deviceVo.getDevno().equals(recordVo.getDevno())) {
                        finalRecordList.add(recordVo);
                    }
                }
            }
            Map<String, DeviceUplinkForm> recordVosMap = finalRecordList.stream().collect(Collectors.toMap(DeviceUplinkForm::getDeveui, s -> s));

            // 需要新增到中间库的list
            List<DeviceUplinkForm> addMidlist = new ArrayList<>();
            // 需要更新到中间库的list
            List<DeviceUplinkForm> updateMidlist = new ArrayList<>();
            // 需要更新到水务库的list
            List<DeviceUplinkForm> updateWaterlist = new ArrayList<>();

            recordVosMap.forEach((s, recordVo) -> {
                DeviceVo deviceVo = midDeviceMap.get(s);
                if (deviceVo != null) {
                    // devid存在
                    recordVo.setDevid(deviceVo.getDevid());
                }
                if (midDeviceUplinkMap.get(s) == null) {
                    // 中间库不存在
                    addMidlist.add(recordVo);
                    updateWaterlist.add(recordVo);
                } else {
                    // 中间库存在
                    // 第三方salt
                    String recordSaltStr = SaltUtils.getRecordSalt(recordVo.getUplinkDate(), recordVo.getJson(), recordVo.getWater(), recordVo.getDeveui(), recordVo.getDevid());
                    DeviceUplinkVo deviceUplinkVo = midDeviceUplinkMap.get(s);
                    // 中间库salt
                    String deviceUplinkSaltStr = SaltUtils.getRecordSalt(deviceUplinkVo.getUplinkDate(), deviceUplinkVo.getJson(), deviceUplinkVo.getWater(), deviceUplinkVo.getDeveui(), deviceUplinkVo.getDevid());
                    // 比较salt
                    if (!deviceUplinkSaltStr.equals(recordSaltStr)) {
                        DeviceUplinkForm copy = BeanUtils.copy(deviceUplinkVo, DeviceUplinkForm.class);
                        copy.setUplinkDate(recordVo.getUplinkDate());
                        copy.setJson(recordVo.getJson());
                        copy.setWater(recordVo.getWater());
                        copy.setDeveui(recordVo.getDeveui());
                        copy.setDevid(recordVo.getDevid());
                        copy.setUrl(recordVo.getUrl());
                        updateMidlist.add(copy);
                        updateWaterlist.add(copy);
                    }
                }
            });

            // 批量新增中间库的设备信息(addMidlist)
            if (!addMidlist.isEmpty()) {
                iMeterDeviceUplinkFactory.adds(addMidlist);
            }
            // 批量修改中间库的设备信息(updateMidlist)
            if (!updateMidlist.isEmpty()) {
                iMeterDeviceUplinkFactory.edits(updateMidlist);
            }
            if (!updateWaterlist.isEmpty()) {
                iWaterClientService.uplinks(BeanUtils.copy(updateWaterlist, DeviceUplinkBo.class), BeanUtils.copy(loginDockingForm, DockingBo.class));
            }
            LOGGER.info("视频直读：抄表信息更新完成--------------");
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }


    }

}
