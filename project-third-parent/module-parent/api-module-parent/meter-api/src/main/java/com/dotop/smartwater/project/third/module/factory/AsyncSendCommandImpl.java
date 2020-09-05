package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.project.third.module.api.factory.*;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.water.constants.TxCode;
import com.dotop.smartwater.project.third.module.api.factory.*;
import com.dotop.smartwater.project.third.module.client.IWaterClientService;
import com.dotop.smartwater.project.third.module.core.constants.DockingConstants;
import com.dotop.smartwater.project.third.module.core.utils.LORA1Utils;
import com.dotop.smartwater.project.third.module.core.water.bo.CommandBo;
import com.dotop.smartwater.project.third.module.core.water.bo.DockingBo;
import com.dotop.smartwater.project.third.module.core.water.form.CommandForm;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DockingVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Component("AsyncSendCommandImpl")
public class AsyncSendCommandImpl implements IThirdObtainFactory {

    private final static Logger LOGGER = LogManager.getLogger("lorahat");

    @Autowired
    private IMeterCommandFactory iMeterCommandFactory;
    @Autowired
    private IWaterClientService iWaterClientService;
    @Resource(name = "LoraFactoryImpl")
    IThirdObtainFactory iLoraFactory;
    @Autowired
    IMeterDockingFactory iMeterDockingFactory;
    @Autowired
    IMeterDeviceUplinkFactory iMeterDeviceUplinkFactory;
    @Autowired
    IMeterDeviceFactory iMeterDeviceFactory;

    @Async("msgExecutor")
    @Override
    public void sendCommandWait(DockingForm dockingForm, DockingForm loginDocking, CommandForm commandForm) throws FrameworkRuntimeException {
        LOGGER.info("开始发送命令-----------------");
        CommandVo commandVo = BeanUtils.copy(commandForm, CommandVo.class);
        try {
            String result = LORA1Utils.setMeterValve(dockingForm, loginDocking, commandForm);
            if (DockingConstants.RESULT_FAIL.equals(result)) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "下发指令失败");
            } else if (DockingConstants.RESULT_SUCCESS.equals(result)) {
                commandVo.setStatus(DockingConstants.COMMAND_FINISH);
                commandVo.setResult(DockingConstants.RESULT_SUCCESS);
                commandVo.setDes(DockingConstants.RESULT_SUCCESS_MSG);
                iMeterCommandFactory.edit(BeanUtils.copy(commandVo, CommandForm.class));
                commandVo.setResult(String.valueOf(TxCode.DOWNLINK_STATUS_SUCCESS));
            }
        } catch (Exception e) {
            //下发失败时，修改命令为完成，和失败
            commandVo.setStatus(DockingConstants.COMMAND_FINISH);
            commandVo.setResult(DockingConstants.RESULT_FAIL);
            commandVo.setDes(DockingConstants.RESULT_FAIL_MSG);
            iMeterCommandFactory.edit(BeanUtils.copy(commandVo, CommandForm.class));
            commandVo.setResult(String.valueOf(TxCode.DOWNLINK_STATUS_FAIL));
            LOGGER.error(LogMsg.to(e, "commandForm:", commandForm));
        } finally {
            try {
                String result = iWaterClientService.downlinkEdit(BeanUtils.copy(commandVo, CommandBo.class), null, BeanUtils.copy(loginDocking, DockingBo.class));
                if (!DockingConstants.RESULT_SUCCESS.equals(result)) {
                    LOGGER.error(LogMsg.to("msg:", "更新到水务库失败", "commandForm:", commandForm));
                }
                if (TxCode.GetWaterCommand  == commandForm.getCommand()) {
                    LOGGER.info("等待一段时间后抄表-----------------");
                    Thread.sleep(LORA1Utils.SLEEP);
                    DeviceUplinkForm searchUplink = new DeviceUplinkForm();
                    searchUplink.setEnterpriseid(loginDocking.getEnterpriseid());
                    List<DeviceUplinkVo> deviceUplinkVos = iMeterDeviceUplinkFactory.list(searchUplink);
                    Map<String, DeviceUplinkVo> deviceUplinkVoMap = deviceUplinkVos.stream().collect(Collectors.toMap(DeviceUplinkVo::getDevno, s -> s));
                    DeviceForm deviceForm = new DeviceForm();
                    deviceForm.setEnterpriseid(loginDocking.getEnterpriseid());
                    deviceForm.setDevno(commandForm.getDevno());
                    DeviceVo deviceVo = iMeterDeviceFactory.get(deviceForm);
                    // 需要使用抄表的docking
                    DockingForm search = new DockingForm();
                    search.setType(DockingConstants.HAT_LORA_GET_METER_VALUE_BY_ID);
                    DockingVo dockingVo = iMeterDockingFactory.get(search);
                    iLoraFactory.deviceUplinkSingelSync(BeanUtils.copy(dockingVo, DockingForm.class), loginDocking, deviceVo, deviceUplinkVoMap);
                }
            } catch (Exception e) {
                LOGGER.error("发送命令发生错误");
                LOGGER.error(LogMsg.to("ex:", e, "commandForm:", commandForm));
            }
        }
        LOGGER.info("发送命令结束-----------------");
    }
}
