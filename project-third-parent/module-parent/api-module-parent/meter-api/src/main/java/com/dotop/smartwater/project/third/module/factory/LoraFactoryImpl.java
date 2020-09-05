package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.project.third.module.api.factory.*;
import com.dotop.smartwater.project.third.module.core.water.bo.*;
import com.dotop.smartwater.project.third.module.core.water.vo.*;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.constants.ModeConstants;
import com.dotop.smartwater.project.third.module.api.factory.*;
import com.dotop.smartwater.project.third.module.api.service.IMeterOwnerService;
import com.dotop.smartwater.project.third.module.api.service.IWaterDeviceService;
import com.dotop.smartwater.project.third.module.api.service.IWaterOwnerService;
import com.dotop.smartwater.project.third.module.client.IWaterClientService;
import com.dotop.smartwater.project.third.module.core.constants.DockingConstants;
import com.dotop.smartwater.project.third.module.core.third.lora.vo.MeterChangeVo;
import com.dotop.smartwater.project.third.module.core.utils.LORA1Utils;
import com.dotop.smartwater.project.third.module.core.utils.SaltUtils;
import com.dotop.smartwater.project.third.module.core.water.bo.*;
import com.dotop.smartwater.project.third.module.core.water.form.CommandForm;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Component("LoraFactoryImpl")
public class LoraFactoryImpl implements IThirdObtainFactory {

    private final static Logger LOGGER = LogManager.getLogger("lorahat");

    @Autowired
    private IMeterDeviceFactory iMeterDeviceFactory;
    @Autowired
    private IMeterDeviceUplinkFactory iMeterDeviceUplinkFactory;
    @Autowired
    private IWaterDeviceService iWaterDeviceService;
    @Autowired
    private IMeterCommandFactory iMeterCommandFactory;
    @Autowired
    private IWaterClientService iWaterClientService;
    @Autowired
    private IMeterDockingFactory iMeterDockingFactory;
    @Autowired
    private IWaterOwnerService iWaterOwnerService;
    @Autowired
    private IMeterOwnerService iMeterOwnerService;

    @Resource(name = "AsyncSendCommandImpl")
    private IThirdObtainFactory asyncSendCommand;

    @Autowired
    private ApplicationContext context;

    private IThirdObtainFactory proxy;

    @PostConstruct
    private void init() {
        proxy = context.getBean("LoraFactoryImpl", IThirdObtainFactory.class);
    }

    @Override
    public void updateDevice(DockingForm dockingForm, DockingForm loginDocking) throws FrameworkRuntimeException {
        try {
            LOGGER.info("LoraFactoryImpl：开始更新设备信息--------------");
            //-------------------------------------------------------同步数据开始------------------
            DeviceForm search = new DeviceForm();
            search.setEnterpriseid(loginDocking.getEnterpriseid());
            List<DeviceVo> deviceVos = iMeterDeviceFactory.list(search);
            //查询水务库设备
            search.setMode(loginDocking.getMode());
            List<DeviceVo> wDeviceVos = iWaterDeviceService.list(BeanUtils.copy(search, DeviceBo.class));
            //分别将，中间库、水务库、查询第三方、的设备转map
            Map<String, DeviceVo> deviceVoMap = deviceVos.stream().collect(Collectors.toMap(DeviceVo::getDevno, s -> s));
            // 水务库所有设备的map
            Map<String, DeviceVo> wDeviceVoMap = wDeviceVos.stream().collect(Collectors.toMap(DeviceVo::getDevno, s -> s));
            List<DeviceForm> syncAdd = new ArrayList<>();
            List<DeviceForm> syncUpdate = new ArrayList<>();
            List<DeviceBo> wSyncUpdate = new ArrayList<>();
            wDeviceVoMap.forEach((s, deviceVo) -> {
                //对比水务库，中间库中没有的设备，将其同步到中间库
                DeviceVo temp = deviceVoMap.get(s);
                if (temp == null) {
                    deviceVo.setId(UuidUtils.getUuid());
                    DeviceForm syncForm = BeanUtils.copy(deviceVo, DeviceForm.class);
                    syncAdd.add(syncForm);
                    deviceVos.add(deviceVo);
                    deviceVoMap.put(s, deviceVo);
                } else if (!temp.getDevid().equals(deviceVo.getDevid()) || !temp.getDeveui().equals(deviceVo.getDeveui())) {
                    String[] modes = deviceVo.getMode().split(",");
                    if (ModeConstants.HAT_LORA.equals(modes[modes.length - 1])) {
                        temp.setDevid(deviceVo.getDevid());
                        temp.setDeveui(deviceVo.getDeveui());
                        syncUpdate.add(BeanUtils.copy(temp, DeviceForm.class));
                    }
                }
            });
            if (!syncAdd.isEmpty()) {
                iMeterDeviceFactory.adds(syncAdd);
            }
            if (!syncUpdate.isEmpty()) {
                iMeterDeviceFactory.edits(syncUpdate);
            }
            // todo 若水务系统删除了设备或者业主，不作反向同步
//            // 中间库同步到水务库（水务库可能误删的设备）
//            deviceVoMap.forEach((s, deviceVo) -> {
//                if (wDeviceVoMap.get(s) == null) {
//                    deviceVo.setProductName(loginDocking.getProductName());
//                    deviceVo.setFactory(loginDocking.getFactory());
//                    deviceVo.setMode(loginDocking.getMode());
//                    wSyncUpdate.add(BeanUtils.copy(deviceVo, DeviceBo.class));
//                }
//            });
//            if (!wSyncUpdate.isEmpty()) {
//                iWaterClientService.deviceAdds(wSyncUpdate, BeanUtils.copy(loginDocking, DockingBo.class));
//            }
            // 同步业主信息
            OwnerBo ownerBo = new OwnerBo();
            ownerBo.setEnterpriseid(loginDocking.getEnterpriseid());
            List<OwnerVo> wOwnerVos = iWaterOwnerService.list(ownerBo);
            List<OwnerVo> ownerVos = iMeterOwnerService.list(ownerBo);
            Map<String, OwnerVo> ownerVoMap = ownerVos.stream().collect(Collectors.toMap(OwnerVo::getUserno, s -> s));
            List<OwnerBo> ownerBos = new ArrayList<>();
            // 水务库业主同步到中间库
            wOwnerVos.forEach(ownerVo -> {
                if (ownerVoMap.get(ownerVo.getUserno()) == null) {
                    ownerVo.setId(UuidUtils.getUuid());
                    ownerVoMap.put(ownerVo.getUserno(), ownerVo);
                    ownerBos.add(BeanUtils.copy(ownerVo, OwnerBo.class));
                }
            });
            if (!ownerBos.isEmpty()) {
                iMeterOwnerService.adds(ownerBos);
            }
            //-------------------------------------------------------同步数据结束------------------
            //-------------------------------------------------------设备及用户同步开始------------------
            // 查询详情
            DockingForm searchDetail = new DockingForm();
            searchDetail.setType(DockingConstants.HAT_LORA_GET_DEVICE_DETAIL_BY_ID);
            DockingVo searchDetailVo = iMeterDockingFactory.get(searchDetail);
            // 查询所有设备
            List<DeviceVo> meterInfoNormals = LORA1Utils.getMeterInfo(dockingForm, searchDetailVo, loginDocking, LORA1Utils.ALL);
            List<DeviceForm> tempNormalForms = BeanUtils.copy(meterInfoNormals, DeviceForm.class);
            tempNormalForms.forEach(deviceForm -> {
                try {
                    proxy.deviceSingelSync(loginDocking, deviceForm, deviceVoMap);
                    proxy.ownerSingleSync(deviceForm, loginDocking, ownerVoMap);
                } catch (FrameworkRuntimeException e) {
                    LOGGER.error(LogMsg.to("ex", e, "deviceForm", deviceForm));
                }
            });
            //-------------------------------------------------------设备及用户同步结束------------------
            LOGGER.info("LoraFactoryImpl：设备更新完毕----------------");
        } catch (Exception e) {

            LOGGER.error(LogMsg.to("ex", e, "dockingForm:", dockingForm, "loginDocking:", loginDocking));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void deviceSingelSync(DockingForm loginDocking, DeviceForm deviceForm, Map<String, DeviceVo> deviceVoMap) {
        try {
            DeviceVo oldVo = deviceVoMap.get(deviceForm.getDevno());
            if (deviceVoMap.get(deviceForm.getDevno()) == null) {
                List<DeviceForm> deviceForms = new ArrayList<>();
                deviceForms.add(deviceForm);
                iMeterDeviceFactory.adds(deviceForms);
                // 新增水表到水务平台
                iWaterClientService.deviceAdds(BeanUtils.copy(deviceForms, DeviceBo.class), BeanUtils.copy(loginDocking, DockingBo.class));
                deviceVoMap.put(deviceForm.getDevno(), BeanUtils.copy(deviceForm, DeviceVo.class));
            } else {
                deviceForm.setDevid(oldVo.getDevid());
                deviceForm.setDevno(oldVo.getDevno());
                deviceForm.setDeveui(oldVo.getDeveui());
                deviceForm.getOwner().setDevid(oldVo.getDevid());
                List<DeviceForm> deviceForms = new ArrayList<>();
                String deviceSalt = SaltUtils.getDeviceSalt(deviceForm.getDevaddr(), "", deviceForm.getDeveui(), deviceForm.getDevno());
                String deviceSalt1 = SaltUtils.getDeviceSalt(oldVo.getDevaddr(), "", oldVo.getDeveui(), oldVo.getDevno());
                if (!deviceSalt.equals(deviceSalt1)) {
                    oldVo.setDevaddr(deviceForm.getDevaddr());
                    deviceForms.add(BeanUtils.copy(oldVo, DeviceForm.class));
                    iMeterDeviceFactory.edits(deviceForms);
                    // 编辑水表信息
                    iWaterClientService.deviceEdits(BeanUtils.copy(deviceForms, DeviceBo.class), BeanUtils.copy(loginDocking, DockingBo.class));
                }
            }
        } catch (Exception e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void ownerSingleSync(DeviceForm deviceForm, DockingForm loginDocking, Map<String, OwnerVo> ownerVoMap) {
        try {
            // 如果水务平台作为抄表系统，才会同步业主信息
            OwnerVo ownerVo = deviceForm.getOwner();
            OwnerVo mOwnerVo = ownerVoMap.get(ownerVo.getUserno());
            //需要换表的业主
            List<OwnerChangeBo> ownerChangeBos = new ArrayList<>();
            if (loginDocking.getSystem().equals(DockingConstants.METER_READING)) {
                List<OwnerBo> addOwnerBos = new ArrayList<>();
                List<OwnerBo> editOwnerBos = new ArrayList<>();
                if (mOwnerVo != null) {
                    String mOwnerSalt = SaltUtils.getOwnerSalt(mOwnerVo.getUserno(), mOwnerVo.getUsername(), mOwnerVo.getUseraddr(), "", mOwnerVo.getDevid());
                    String ownerSalt = SaltUtils.getOwnerSalt(ownerVo.getUserno(), ownerVo.getUsername(), ownerVo.getUseraddr(), "", ownerVo.getDevid());
                    if (!mOwnerSalt.equals(ownerSalt)) {
                        mOwnerVo.setUsername(ownerVo.getUsername());
                        mOwnerVo.setUseraddr(ownerVo.getUseraddr());
                        mOwnerVo.setUserphone(ownerVo.getUserphone());
                        editOwnerBos.add(BeanUtils.copy(mOwnerVo, OwnerBo.class));
                    }
                } else {
                    // 新增业主
                    addOwnerBos.add(BeanUtils.copy(ownerVo, OwnerBo.class));
                    OwnerChangeBo ownerChangeBo = new OwnerChangeBo();
                    ownerChangeBo.setOwnerid(ownerVo.getOwnerid());
                    ownerChangeBo.setDevid(ownerVo.getDevid());
                    ownerChangeBo.setOldDevid(ownerVo.getDevid());
                    // 新增业主的同时，绑定水表信息需要调换表接口
                    ownerChangeBos.add(ownerChangeBo);
                    ownerVoMap.put(ownerVo.getUserno(), ownerVo);
                }
                if (!addOwnerBos.isEmpty()) {
                    iMeterOwnerService.adds(addOwnerBos);
                }
                if (!editOwnerBos.isEmpty()) {
                    iMeterOwnerService.edits(editOwnerBos, loginDocking.getEnterpriseid());
                }
                // 设备id不相同，需要换表
                OwnerBo ownerBo = new OwnerBo();
                ownerBo.setEnterpriseid(loginDocking.getEnterpriseid());
                ownerBo.setUserno(ownerVo.getUserno());
                // 查询当前水表在水务系统中的业主， ownerVo为查询回来的水表所对应的业主
                OwnerVo oldOwner = iMeterOwnerService.get(ownerBo);
                if (oldOwner != null && !ownerVo.getDevid().equals(oldOwner.getDevid())) {
                    DockingForm search = new DockingForm();
                    search.setType(DockingConstants.HAT_LORA_GET_METER_CHANGE);
                    DockingVo dockingVo = iMeterDockingFactory.get(search);
                    // 查询换表记录作校验
                    List<MeterChangeVo> meterChange = LORA1Utils.getMeterChange(BeanUtils.copy(dockingVo, DockingForm.class), loginDocking);
                    // 若该水表存在换表记录则换表
                    if (meterChange.stream().anyMatch(s -> s.getMeterId().equals(Integer.parseInt(deviceForm.getDevno())))) {
                        LOGGER.info(LogMsg.to("新表：", deviceForm, "旧表：", oldOwner));
                        // 可以换表
                        OwnerChangeBo ownerChangeBo = new OwnerChangeBo();
                        ownerChangeBo.setOwnerid(oldOwner.getOwnerid());
                        ownerChangeBo.setDevid(deviceForm.getDevid());
                        ownerChangeBo.setOldDevid(oldOwner.getDevid());
                        oldOwner.setDevid(deviceForm.getDevid());
                        oldOwner.setThirdid(deviceForm.getDevno());
                        editOwnerBos.clear();
                        editOwnerBos.add(BeanUtils.copy(oldOwner, OwnerBo.class));
                        ownerChangeBos.add(ownerChangeBo);
                    } else {
                        LOGGER.error(LogMsg.to("新表：", deviceForm, "旧表：", oldOwner));
                    }
                }
                if (!editOwnerBos.isEmpty()) {
                    iMeterOwnerService.edits(editOwnerBos, loginDocking.getEnterpriseid());
                }
                if (!addOwnerBos.isEmpty()) {
                    iWaterClientService.ownerAdds(BeanUtils.copy(addOwnerBos, OwnerBo.class), BeanUtils.copy(loginDocking, DockingBo.class));
                }
                if (!editOwnerBos.isEmpty()) {
                    iWaterClientService.ownerEdits(BeanUtils.copy(editOwnerBos, OwnerBo.class), BeanUtils.copy(loginDocking, DockingBo.class));
                }
                if (!ownerChangeBos.isEmpty()) {
                    iWaterClientService.ownerChanges(ownerChangeBos, BeanUtils.copy(loginDocking, DockingBo.class));
                }
            } else if (loginDocking.getSystem().equals(DockingConstants.CHARGE) && LORA1Utils.SYNC) {
                // 若水务平台作为为收费系统，则需要同步业主信息到第三方平台， SYNC可作为配置项，是否同步数据到第三方
                if (mOwnerVo != null) {
                    String mOwnerSalt = SaltUtils.getOwnerSalt(mOwnerVo.getUserno(), mOwnerVo.getUsername(), mOwnerVo.getUseraddr(), "", "");
                    String ownerSalt = SaltUtils.getOwnerSalt(ownerVo.getUserno(), ownerVo.getUsername(), ownerVo.getUseraddr(), "", "");
                    if (!mOwnerSalt.equals(ownerSalt)) {
                        DockingForm search = new DockingForm();
                        search.setType(DockingConstants.HAT_LORA_SET_USER_INFO_BY_ID);
                        DockingVo dockingVo = iMeterDockingFactory.get(search);
                        deviceForm.getOwner().setUsername(mOwnerVo.getUsername());
                        deviceForm.getOwner().setUseraddr(mOwnerVo.getUseraddr());
                        if (LORA1Utils.setUserInfo(BeanUtils.copy(dockingVo, DockingForm.class), loginDocking, deviceForm, LORA1Utils.OPT_TYPE_EDIT)) {
                            LOGGER.info("更新业主信息到第三方成功");
                        }
                    }
                }
                // todo 删除业主时，不同步到第三方
            }
        } catch (Exception e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void updateDeviceUplink(DockingForm dockingForm, DockingForm loginDocking) throws FrameworkRuntimeException {
        try {
            LOGGER.info("LoraFactoryImpl：开始更新抄表数据--------------");
            // 查询中间库设备
            DeviceForm search = new DeviceForm();
            search.setEnterpriseid(loginDocking.getEnterpriseid());
            List<DeviceVo> deviceVos = iMeterDeviceFactory.list(search);
            // 查询中间库设备最新抄表记录
            DeviceUplinkForm searchUplink = new DeviceUplinkForm();
            searchUplink.setEnterpriseid(loginDocking.getEnterpriseid());
            List<DeviceUplinkVo> deviceUplinkVos = iMeterDeviceUplinkFactory.list(searchUplink);
            //查询水务库设备
            DeviceForm deviceForm = new DeviceForm();
            deviceForm.setMode(loginDocking.getMode());
            deviceForm.setEnterpriseid(loginDocking.getEnterpriseid());
            List<DeviceVo> wDeviceVos = iWaterDeviceService.list(BeanUtils.copy(deviceForm, DeviceBo.class));
            // 转map
            Map<String, DeviceVo> deviceVoMap = deviceVos.stream().collect(Collectors.toMap(DeviceVo::getDevno, s -> s));
            Map<String, DeviceVo> wDeviceVoMap = wDeviceVos.stream().collect(Collectors.toMap(DeviceVo::getDevno, s -> s));
            Map<String, DeviceUplinkVo> deviceUplinkVoMap = deviceUplinkVos.stream().collect(Collectors.toMap(DeviceUplinkVo::getDevno, s -> s));
            deviceVoMap.forEach((s, deviceVo) -> {
                try {
                    if (wDeviceVoMap.get(s) != null) {
                        proxy.deviceUplinkSingelSync(dockingForm, loginDocking, deviceVo, deviceUplinkVoMap);
                    }
                } catch (Exception e) {
                    LOGGER.error(LogMsg.to("ex", e, "deviceVo", deviceVo));
                }
            });
            LOGGER.info("LoraFactoryImpl：抄表更新完毕--------------");
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e, "dockingForm:", dockingForm, "loginDocking:", loginDocking));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void deviceUplinkSingelSync(DockingForm dockingForm, DockingForm loginDocking, DeviceVo deviceVo, Map<String, DeviceUplinkVo> deviceUplinkVoMap) {
        try {
            // todo 当第三方id为空时，将第三方的meterId视为meterNo
            String meterId = StringUtils.isNoneBlank(deviceVo.getThirdid()) ? deviceVo.getThirdid() : deviceVo.getDevno();
            List<DeviceUplinkForm> deviceUplinkForms = LORA1Utils.getMeterValue(dockingForm, loginDocking, Integer.parseInt(meterId));
            if (!deviceUplinkForms.isEmpty()) {
                DeviceUplinkForm deviceUplinkForm = deviceUplinkForms.get(0);
                // 给查询回来的上报记录设置其对应设备的基本信息
                deviceUplinkForm.setThirdid(meterId);
                deviceUplinkForm.setDevid(deviceVo.getDevid());
                deviceUplinkForm.setDevno(deviceVo.getDevno());
                deviceUplinkForm.setDeveui(deviceVo.getDeveui());
                DeviceUplinkVo deviceUplinkVo = deviceUplinkVoMap.get(deviceUplinkForm.getDevno());
                List<DeviceUplinkForm> addForms = new ArrayList<>();
                List<DeviceUplinkForm> updateForms = new ArrayList<>();
                List<DeviceUplinkForm> wAddForms = new ArrayList<>();
                // 中间库中，没有该设备的上行数据
                if (deviceUplinkVo == null) {
                    addForms.add(deviceUplinkForm);
                    DeviceUplinkForm wUpdate = BeanUtils.copy(deviceUplinkForm, DeviceUplinkForm.class);
                    wUpdate.setId(UuidUtils.getUuid());
                    wAddForms.add(wUpdate);
                } else {
                    String saltStr = SaltUtils.getRecordSalt(deviceUplinkForm.getUplinkDate(), deviceUplinkForm.getJson(), deviceUplinkForm.getWater(), deviceUplinkForm.getDeveui(), deviceUplinkForm.getDevno());
                    String saltStr2 = SaltUtils.getRecordSalt(deviceUplinkVo.getUplinkDate(), deviceUplinkVo.getJson(), deviceUplinkVo.getWater(), deviceUplinkVo.getDeveui(), deviceUplinkVo.getDevno());
                    if (!saltStr.equals(saltStr2)) {
                        //如果有变化则更新，同时更新到水务库
                        DeviceUplinkForm update = BeanUtils.copy(deviceUplinkVo, DeviceUplinkForm.class);
                        update.setUplinkDate(deviceUplinkForm.getUplinkDate());
                        update.setWater(deviceUplinkForm.getWater());
                        update.setJson(deviceUplinkForm.getJson());
                        updateForms.add(update);
                        DeviceUplinkForm wUpdate = BeanUtils.copy(update, DeviceUplinkForm.class);
                        wUpdate.setId(UuidUtils.getUuid());
                        wAddForms.add(wUpdate);
                    }
                }
                if (!updateForms.isEmpty()) {
                    iMeterDeviceUplinkFactory.edits(updateForms);
                }
                if (!addForms.isEmpty()) {
                    iMeterDeviceUplinkFactory.adds(addForms);
                }
                if (!wAddForms.isEmpty()) {
                    iWaterClientService.uplinks(BeanUtils.copy(wAddForms, DeviceUplinkBo.class), BeanUtils.copy(loginDocking, DockingBo.class));
                }
            } else {
                LOGGER.info(LogMsg.to("deviceVo:", deviceVo), "无抄表数据");
            }
        } catch (Exception e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public CommandVo sendCommand(DockingForm dockingForm, DockingForm loginDocking, CommandForm commandForm) throws FrameworkRuntimeException {
        //查询设备
        DeviceBo deviceBo = new DeviceBo();
        deviceBo.setEnterpriseid(commandForm.getEnterpriseid());
        deviceBo.setDevid(commandForm.getDevid());
        DeviceVo deviceVo = iWaterDeviceService.get(deviceBo);
        if (deviceVo == null) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "找不到该水表");
        }
        if (dockingForm == null || loginDocking == null) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "该接口数据库尚未配置完成");
        }
        try {
            //保存任务日志
            commandForm.setClientid(UuidUtils.getUuid());
            commandForm.setDeveui(deviceVo.getDeveui());
            commandForm.setId(UuidUtils.getUuid());
            commandForm.setDevno(deviceVo.getDevno());
            //新增记录，默认处理中
            commandForm.setStatus(DockingConstants.COMMAND_DOING);
            CommandVo commandVo = iMeterCommandFactory.add(commandForm);
            //向第三方发送命令
            // 等待，有结果之后设置状态，需要先返回结果
            asyncSendCommand.sendCommandWait(dockingForm, loginDocking, commandForm);
            return commandVo;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e, "commandForm:", commandForm));
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }
}
