package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.constants.ModeConstants;
import com.dotop.smartwater.project.module.core.water.constants.TxCode;
import com.dotop.smartwater.project.third.module.api.factory.IMeterCommandFactory;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDeviceFactory;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDeviceUplinkFactory;
import com.dotop.smartwater.project.third.module.api.factory.IThirdObtainFactory;
import com.dotop.smartwater.project.third.module.api.service.IWaterDeviceService;
import com.dotop.smartwater.project.third.module.client.IWaterClientService;
import com.dotop.smartwater.project.third.module.core.constants.DockingConstants;
import com.dotop.smartwater.project.third.module.core.utils.Nb1Utils;
import com.dotop.smartwater.project.third.module.core.utils.SaltUtils;
import com.dotop.smartwater.project.third.module.core.water.bo.CommandBo;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.third.module.core.water.bo.DockingBo;
import com.dotop.smartwater.project.third.module.core.water.form.CommandForm;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 */
@Component("XmFactoryImpl")
public class XmFactoryImpl implements IThirdObtainFactory {

    private final static Logger LOGGER = LogManager.getLogger("xm");

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

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void updateDevice(DockingForm dockingForm, DockingForm loginDocking) throws FrameworkRuntimeException {
        try {
            LOGGER.info("NbFactoryImpl：开始更新设备信息--------------");
            // 查询远程设备
            List<DeviceForm> deviceFroms = Nb1Utils.getDeviceInfo(dockingForm, loginDocking);
            //查询中间库设备
            DeviceForm search = new DeviceForm();
            search.setEnterpriseid(loginDocking.getEnterpriseid());
            List<DeviceVo> deviceVos = iMeterDeviceFactory.list(search);
            //查询水务库设备
            search.setMode(loginDocking.getMode());
            List<DeviceVo> wDeviceVos = iWaterDeviceService.list(BeanUtils.copy(search, DeviceBo.class));
            //分别将，中间库、水务库、查询第三方、的设备转map
            Map<String, DeviceVo> deviceVoMap = deviceVos.stream().collect(Collectors.toMap(DeviceVo::getDeveui, s -> s));
            // 水务库所有设备的map
            Map<String, DeviceVo> wDeviceVoMap = wDeviceVos.stream().collect(Collectors.toMap(DeviceVo::getDeveui, s -> s));

            //中间库新增
            List<DeviceForm> addForms = new ArrayList<>();
            //中间库更新
            List<DeviceForm> updateForms = new ArrayList<>();
            //水务库新增
            List<DeviceForm> wAddForms = new ArrayList<>();
            //水务库更新
            List<DeviceForm> wUpdateForms = new ArrayList<>();
            //TODO 删除暂时不处理
            //同步水务平台数据
            List<DeviceForm> syncAdd = new ArrayList<>();
            List<DeviceForm> syncUpdate = new ArrayList<>();
            wDeviceVoMap.forEach((s, deviceVo) -> {
                //对比水务库，中间库中没有的设备，将其同步到中间库
                DeviceVo temp = deviceVoMap.get(s);
                if (temp == null) {
                    deviceVo.setId(UuidUtils.getUuid());
                    DeviceForm syncForm = BeanUtils.copy(deviceVo, DeviceForm.class);
                    syncAdd.add(syncForm);
                    deviceVos.add(deviceVo);
                    deviceVoMap.put(s, deviceVo);
                } else if (!temp.getDevid().equals(deviceVo.getDevid()) || !temp.getDevno().equals(deviceVo.getDevno())) {
                    // 如果水务库删除了设备，又添加了相同设备，使用水务库设备id覆盖中间库设备id
                    String[] modes = deviceVo.getMode().split(",");
                    switch (modes[modes.length - 1]) {
                        case ModeConstants.DX_LORA:
                        case ModeConstants.DX_NB_DX:
                        case ModeConstants.DX_NB_YD:
                        case ModeConstants.HAT_NB:
                            temp.setDevid(deviceVo.getDevid());
                            temp.setDevno(deviceVo.getDevno());
                            syncUpdate.add(BeanUtils.copy(temp, DeviceForm.class));
                            break;
                        default:
                            break;
                    }
                }
            });
            if (!syncAdd.isEmpty()) {
                iMeterDeviceFactory.adds(syncAdd);
            }
            if (!syncUpdate.isEmpty()) {
                iMeterDeviceFactory.edits(syncUpdate);
            }
            //筛选合法数据
            deviceFroms = iMeterDeviceFactory.check(deviceFroms, loginDocking);
            // 查询的第三方的设备的map
            Map<String, DeviceForm> deviceFromMap = deviceFroms.stream().collect(Collectors.toMap(DeviceForm::getDeveui, s -> s));
            //筛选出，查询出来的数据，那些需要更新或新增
            deviceFromMap.forEach((s, deviceFrom) -> {
                //对比查询回来的数据，中间库没有的数据则更新
                if (deviceVoMap.get(s) == null) {
                    addForms.add(deviceFrom);
                    wAddForms.add(deviceFrom);
                } else {
                    //如果水务库误操作删除了设备，则将其添加
                    if (wDeviceVoMap.get(s) == null) {
                        wAddForms.add(deviceFrom);
                    }
                    String saltStr = SaltUtils.getDeviceSalt(deviceFrom.getDevaddr(), deviceFrom.getJson(), deviceFrom.getDeveui(), "");
                    DeviceVo deviceVo = deviceVoMap.get(s);
                    String saltStr2 = SaltUtils.getDeviceSalt(deviceVo.getDevaddr(), deviceVo.getJson(), deviceFrom.getDeveui(), "");
                    if (!saltStr.equals(saltStr2)) {
                        DeviceForm update = BeanUtils.copy(deviceVoMap.get(s), DeviceForm.class);
                        update.setDevno(deviceFrom.getDevno());
                        update.setDevaddr(deviceFrom.getDevaddr());
                        update.setJson(deviceFrom.getJson());
                        updateForms.add(update);
                        //若水务库存在该设备，则随中间库一同更新
                        if (wDeviceVoMap.get(s) != null) {
                            wUpdateForms.add(update);
                        }
                    }
                }
            });
            LOGGER.info(LogMsg.to("updateForms", updateForms));
            if (!updateForms.isEmpty()) {
                iMeterDeviceFactory.edits(updateForms);
            }
            LOGGER.info(LogMsg.to("addForms", addForms));
            if (!addForms.isEmpty()) {
                iMeterDeviceFactory.adds(addForms);
            }
            LOGGER.info(LogMsg.to("wAddForms", wAddForms));
            if (!wAddForms.isEmpty()) {
                wAddForms.forEach(deviceForm -> {
                    if (StringUtils.isBlank(deviceForm.getEnterpriseid())) {
                        LOGGER.error(LogMsg.to("NbFactoryImpl：企业id为空", deviceForm));
                    }
                });
                iWaterClientService.deviceAdds(BeanUtils.copy(wAddForms, DeviceBo.class), BeanUtils.copy(loginDocking, DockingBo.class));
            }
            LOGGER.info(LogMsg.to("wUpdateForms", wUpdateForms));
            if (!wUpdateForms.isEmpty()) {
                wUpdateForms.forEach(deviceForm -> {
                    if (StringUtils.isBlank(deviceForm.getEnterpriseid())) {
                        LOGGER.error(LogMsg.to("NbFactoryImpl：企业id为空", deviceForm));
                    }
                });
                iWaterClientService.deviceEdits(BeanUtils.copy(wUpdateForms, DeviceBo.class), BeanUtils.copy(loginDocking, DockingBo.class));
            }
            LOGGER.info("NbFactoryImpl：设备更新完毕----------------");
        } catch (Exception e) {
            LOGGER.error(e);
            LOGGER.error(LogMsg.to("ex", e, "dockingForm:", dockingForm, "loginDocking:", loginDocking));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void updateDeviceUplink(DockingForm dockingForm, DockingForm loginDocking) throws FrameworkRuntimeException {
        try {
            LOGGER.info(LogMsg.to("msg", "NbFactoryImpl：开始更新抄表数据--------------", "dockingForm:", dockingForm, "loginDocking:", loginDocking));
            List<DeviceUplinkForm> deviceUplinkFroms = Nb1Utils.getDeviceCurrentData(dockingForm, loginDocking);
            LOGGER.info(LogMsg.to("msg", "0"));
            // 查询中间库设备最新抄表记录
            DeviceUplinkForm search = new DeviceUplinkForm();
            search.setEnterpriseid(loginDocking.getEnterpriseid());
            List<DeviceUplinkVo> deviceUplinkVos = iMeterDeviceUplinkFactory.list(search);
            LOGGER.info(LogMsg.to("msg", "1"));
            //查询水务库设备
            DeviceForm deviceForm = new DeviceForm();
            deviceForm.setMode(loginDocking.getMode());
            deviceForm.setEnterpriseid(loginDocking.getEnterpriseid());
            List<DeviceVo> wDeviceVos = iWaterDeviceService.list(BeanUtils.copy(deviceForm, DeviceBo.class));
            LOGGER.info(LogMsg.to("msg", "2"));
            //查询中间库的设备
            List<DeviceVo> deviceVos = iMeterDeviceFactory.list(deviceForm);
            LOGGER.info(LogMsg.to("msg", "3"));
            Map<String, DeviceVo> deviceVoMap = deviceVos.stream().collect(Collectors.toMap(DeviceVo::getDeveui, s -> s));
            LOGGER.info(LogMsg.to("deviceVoMap", deviceVoMap.size()));
            // 查询的水务库的设备信息
            Map<String, DeviceVo> wDeviceVoMap = wDeviceVos.stream().collect(Collectors.toMap(DeviceVo::getDeveui, s -> s));
            LOGGER.info(LogMsg.to("deviceVoMap", wDeviceVoMap.size()));
            // 查询的中间库的抄表信息
            Map<String, DeviceUplinkVo> deviceUplinkVoMap = deviceUplinkVos.stream().collect(Collectors.toMap(DeviceUplinkVo::getDeveui, s -> s));
            LOGGER.info(LogMsg.to("deviceVoMap", deviceUplinkVoMap.size()));
            // 查询的第三方的抄表信息
            Map<String, DeviceUplinkForm> deviceUplinkFormMap = deviceUplinkFroms.stream().collect(Collectors.toMap(DeviceUplinkForm::getDeveui, s -> s));
            LOGGER.info(LogMsg.to("deviceVoMap", deviceUplinkFormMap.size()));
            //中间库新增的抄表信息
            List<DeviceUplinkForm> addForms = new ArrayList<>();
            List<DeviceUplinkForm> updateForms = new ArrayList<>();
            List<DeviceUplinkForm> wAddForms = new ArrayList<>();
            List<DeviceForm> updateMeterForms = new ArrayList<>();
            List<DeviceForm> updateWaterForms = new ArrayList<>();
            //TODO 删除暂时不处理
            List<DeviceForm> delForms = new ArrayList<>();
            deviceUplinkFormMap.forEach((s, deviceUplinkForm) -> {
                if (deviceUplinkForm == null) {
                    LOGGER.info(LogMsg.to("s", s));
                }
                if (wDeviceVoMap.get(s) != null) {
                    //由于查询回来无devno，需要再此处set
                    deviceUplinkForm.setDevno(wDeviceVoMap.get(s).getDevno());
                    deviceUplinkForm.setDevid(wDeviceVoMap.get(s).getDevid());
                } else if (deviceVoMap.get(s) != null) {
                    //由于查询回来无devno，需要再此处set
                    deviceUplinkForm.setDevno(deviceVoMap.get(s).getDevno());
                    deviceUplinkForm.setDevid(deviceVoMap.get(s).getDevid());
                }
                //如果第三方查询到的抄表数据，对应中间库存在设备
                if (deviceVoMap.get(s) != null) {
                    String saltStr = SaltUtils.getNBDeviceSalt(deviceUplinkForm.getAgreement(), deviceUplinkForm.getDelivery(), deviceUplinkForm.getTapstatus(), deviceUplinkForm.getDeveui());
                    DeviceVo deviceVo = deviceVoMap.get(s);
                    String saltStr2 = SaltUtils.getNBDeviceSalt(deviceVo.getAgreement(), deviceVo.getDelivery(), deviceVo.getTapstatus(), deviceVo.getDeveui());
                    if (!saltStr.equals(saltStr2)) {
                        //更新设备表
                        DeviceForm temp = BeanUtils.copy(deviceVo, DeviceForm.class);
                        temp.setDevno(deviceUplinkForm.getDevno());
                        temp.setAgreement(deviceUplinkForm.getAgreement());
                        temp.setDelivery(deviceUplinkForm.getDelivery());
                        temp.setTapstatus(deviceUplinkForm.getTapstatus());
                        temp.setTaptype(deviceUplinkForm.getTaptype());
                        updateMeterForms.add(temp);
                        updateWaterForms.add(temp);
                    }
                }
                //如果中间库抄表记录中不存在该抄表数据
                if (deviceUplinkVoMap.get(s) == null) {
                    //从中间库或水务库中，查找设备，并添加对应的devid
                    if (deviceVoMap.get(deviceUplinkForm.getDeveui()) != null) {
                        deviceUplinkForm.setDevid(deviceVoMap.get(deviceUplinkForm.getDeveui()).getDevid());
                    }
                    if (wDeviceVoMap.get(deviceUplinkForm.getDeveui()) != null) {
                        deviceUplinkForm.setDevid(wDeviceVoMap.get(deviceUplinkForm.getDeveui()).getDevid());
                    }
                    if (StringUtils.isNotBlank(deviceUplinkForm.getDevid())) {
                        addForms.add(deviceUplinkForm);
                        wAddForms.add(deviceUplinkForm);
                    }
                } else {
                    //如果中间库中已存在该设备的抄表记录
                    String saltStr = SaltUtils.getRecordSalt(deviceUplinkForm.getUplinkDate(), deviceUplinkForm.getJson(), deviceUplinkForm.getWater(), deviceUplinkForm.getDeveui(), deviceUplinkForm.getDevid());
                    DeviceUplinkVo deviceUplinkVo = deviceUplinkVoMap.get(s);
                    String saltStr2 = SaltUtils.getRecordSalt(deviceUplinkVo.getUplinkDate(), deviceUplinkVo.getJson(), deviceUplinkVo.getWater(), deviceUplinkVo.getDeveui(), deviceUplinkVo.getDevid());
                    if (!saltStr.equals(saltStr2)) {
                        //如果有变化则更新，同时更新到水务库
                        DeviceUplinkForm update = BeanUtils.copy(deviceUplinkVo, DeviceUplinkForm.class);
                        update.setDevno(deviceUplinkForm.getDevno());
                        update.setUplinkDate(deviceUplinkForm.getUplinkDate());
                        update.setWater(deviceUplinkForm.getWater());
                        update.setJson(deviceUplinkForm.getJson());
                        update.setDevid(deviceUplinkForm.getDevid());
                        updateForms.add(update);
                        DeviceUplinkForm wUpdate = BeanUtils.copy(update, DeviceUplinkForm.class);
                        wUpdate.setId(UuidUtils.getUuid());
                        wAddForms.add(wUpdate);
                    }
                }
            });
            LOGGER.info(LogMsg.to("updateForms", updateForms.size()));
            if (!updateForms.isEmpty()) {
                iMeterDeviceUplinkFactory.edits(updateForms);
            }
            LOGGER.info(LogMsg.to("addForms", addForms.size()));
            if (!addForms.isEmpty()) {
                iMeterDeviceUplinkFactory.adds(addForms);
            }
            LOGGER.info(LogMsg.to("updateMeterForms", updateMeterForms.size()));
            if (!updateMeterForms.isEmpty()) {
                // 更新中间库设备表协议类型等
                iMeterDeviceFactory.edits(updateMeterForms);
            }
            LOGGER.info(LogMsg.to("wAddForms", wAddForms.size()));
            if (!wAddForms.isEmpty()) {
                iWaterClientService.uplinks(BeanUtils.copy(wAddForms, DeviceUplinkBo.class), BeanUtils.copy(loginDocking, DockingBo.class));
            }
            LOGGER.info(LogMsg.to("updateWaterForms", updateWaterForms.size()));
            if (!updateWaterForms.isEmpty()) {
                iWaterClientService.deviceEdits(BeanUtils.copy(updateWaterForms, DeviceBo.class), BeanUtils.copy(loginDocking, DockingBo.class));
            }
            LOGGER.info("NbFactoryImpl：抄表更新完毕--------------");
        } catch (Exception e) {
            LOGGER.error(e);
            LOGGER.error(LogMsg.to(e, "dockingForm:", dockingForm, "loginDocking:", loginDocking));
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
            //新增记录，默认处理中
            commandForm.setStatus(DockingConstants.COMMAND_DOING);
            CommandVo commandVo = iMeterCommandFactory.add(commandForm);
            //向第三方发送命令
            String result = Nb1Utils.sendCommand(dockingForm, loginDocking, commandForm);
            if (DockingConstants.RESULT_FAIL.equals(result)) {
                //下发失败时，修改命令为完成，和失败
                commandVo.setStatus(DockingConstants.COMMAND_FINISH);
                commandVo.setResult(DockingConstants.RESULT_FAIL);
                iMeterCommandFactory.edit(BeanUtils.copy(commandVo, CommandForm.class));
                throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "下发指令失败");
            }
            return commandVo;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e, "commandForm:", commandForm));
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public CommandVo cancelCommand(DockingForm dockingForm, DockingForm loginDocking, CommandForm commandForm) throws FrameworkRuntimeException {
        //TODO 取消命令暂时不可用
        //查询命令
        CommandVo commandVo = iMeterCommandFactory.get(commandForm);
        if (commandVo == null) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "该命令不存在");
        }
        if (dockingForm == null || loginDocking == null) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "该接口数据库尚未配置完成");
        }
        try {
            if (DockingConstants.COMMAND_FINISH.equals(commandVo.getStatus()) || StringUtils.isNotBlank(commandVo.getResult())) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "该命令已完成");
            }
            //向第三方发送命令
            String result = Nb1Utils.cancelCommand(dockingForm, loginDocking, commandForm);
            if (DockingConstants.RESULT_FAIL.equals(result)) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "下发指令失败");
            } else {
                commandVo.setStatus(DockingConstants.COMMAND_CANCEL);
                iMeterCommandFactory.edit(BeanUtils.copy(commandVo, CommandForm.class));
            }
            return commandVo;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e, "commandForm:", commandForm));
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void updateCommandStatus(DockingForm dockingForm, DockingForm loginDocking, CommandForm commandForm) throws FrameworkRuntimeException {
        //查询命令
        CommandVo commandVo = iMeterCommandFactory.get(commandForm);
        if (commandVo == null) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "该命令不存在");
        }
        if (dockingForm == null || loginDocking == null) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "该接口数据库尚未配置完成");
        }
        try {
            //向第三方查询结果
            CommandVo commandVo1 = Nb1Utils.getCommandStatus(dockingForm, loginDocking, commandForm);
            if (StringUtils.isNotBlank(commandVo1.getResult())) {
                iMeterCommandFactory.edit(BeanUtils.copy(commandVo1, CommandForm.class));
                if (commandVo1.getResult().equals(DockingConstants.RESULT_SUCCESS)) {
                    commandVo1.setResult(String.valueOf(TxCode.DOWNLINK_STATUS_SUCCESS));
                } else if (commandVo1.getResult().equals(DockingConstants.RESULT_FAIL)) {
                    commandVo1.setResult(String.valueOf(TxCode.DOWNLINK_STATUS_FAIL));
                }
                String result = iWaterClientService.downlinkEdit(BeanUtils.copy(commandVo1, CommandBo.class), null, BeanUtils.copy(loginDocking, DockingBo.class));
                if (!DockingConstants.RESULT_SUCCESS.equals(result)) {
                    throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, "更新失败");
                }
            }
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e, "commandForm:", commandForm));
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }
}
