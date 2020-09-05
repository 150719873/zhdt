package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.TxCode;
import com.dotop.smartwater.project.module.core.water.form.customize.OperationForm;
import com.dotop.smartwater.project.third.module.api.factory.IMeterCommandFactory;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDockingFactory;
import com.dotop.smartwater.project.third.module.api.factory.IStandardCommandFactory;
import com.dotop.smartwater.project.third.module.api.service.IMeterDeviceService;
import com.dotop.smartwater.project.third.module.api.service.IWaterDeviceService;
import com.dotop.smartwater.project.third.module.client.IWaterClientService;
import com.dotop.smartwater.project.third.module.core.constants.DockingConstants;
import com.dotop.smartwater.project.third.module.core.water.bo.CommandBo;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.third.module.core.water.bo.DockingBo;
import com.dotop.smartwater.project.third.module.core.water.form.CommandForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DockingVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.dotop.smartwater.project.third.module.core.constants.DockingConstants.*;

/**
 *
 */
@Component
public class StandardCommandFactoryImpl implements IStandardCommandFactory {

    private final static Logger LOGGER = LogManager.getLogger(StandardCommandFactoryImpl.class);

    @Autowired
    private IMeterDockingFactory iMeterDockingFactory;

    @Autowired
    private IWaterClientService iWaterClientService;

    @Autowired
    private IMeterDeviceService iMeterDeviceService;

    @Autowired
    private IWaterDeviceService iWaterDeviceService;

    @Autowired
    private IMeterCommandFactory iMeterCommandFactory;

    @Override
    public CommandVo sendCommand(OperationForm operationForm) throws FrameworkRuntimeException {
        // 获取配置的登录信息
        DockingForm dockingForm = new DockingForm();
        dockingForm.setType(DockingConstants.REMOTE_API);
        dockingForm.setEnterpriseid(operationForm.getEnterpriseid());
        dockingForm.setUsername(operationForm.getUsername());
        DockingVo dockingVo = iMeterDockingFactory.get(dockingForm);
        if (dockingVo == null) {
            LOGGER.error(LogMsg.to("dockingForm", dockingForm));
            throw new FrameworkRuntimeException(ResultCode.NOTFINDWATER, "账户密码错误");
        }

        // 校验设备是否存在
        DeviceBo deviceBo = new DeviceBo();
        deviceBo.setEnterpriseid(operationForm.getEnterpriseid());
        deviceBo.setDevno(operationForm.getDevNo());
//        DeviceVo deviceVo = iMeterDeviceService.get(deviceBo);
//        DeviceVo wDeviceVo = iWaterDeviceService.get(deviceBo);
        DeviceVo deviceVo = iWaterDeviceService.get(deviceBo);
        if (deviceVo == null) {
            throw new FrameworkRuntimeException(ResultCode.NOTFINDWATER, "设备不存在");
        }
        LOGGER.info(LogMsg.to("deviceVo", deviceVo));
        //向水务平台发送命令准备
        CommandBo commandBo = new CommandBo();
        //  水务系统1关阀，2开阀
        if (operationForm.getType() == TxCode.OpenCommand) {
            commandBo.setCommand(TxCode.OpenCommand);
        } else if (operationForm.getType() == TxCode.CloseCommand) {
            commandBo.setCommand(TxCode.CloseCommand);
        } else {
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, "未知命令");
        }
        commandBo.setDevid(deviceVo.getDevid());
        //保存中间库日志
        CommandForm commandForm = BeanUtils.copy(commandBo, CommandForm.class);
        commandForm.setDeveui(deviceVo.getDeveui());
        commandForm.setId(UuidUtils.getUuid());
        commandForm.setEnterpriseid(deviceVo.getEnterpriseid());
        //新增记录，默认处理中
        commandForm.setStatus(COMMAND_READY);
        CommandVo commandVo = iMeterCommandFactory.add(commandForm);
        try {
            CommandVo downlink = iWaterClientService.downlink(commandBo, null, BeanUtils.copy(dockingVo, DockingBo.class));
            if (StringUtils.isNotBlank(downlink.getClientid())) {
                commandVo.setClientid(downlink.getClientid());
                commandVo.setStatus(COMMAND_DOING);
                CommandForm syn = BeanUtils.copy(commandVo, CommandForm.class);
                iMeterCommandFactory.edit(syn);
                return commandVo;
            } else {
                throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, "下发命令失败");
            }
        } catch (Exception e) {
            commandVo.setDes(RESULT_FAIL_MSG);
            commandVo.setStatus(COMMAND_FINISH);
            commandVo.setResult(RESULT_FAIL);
            CommandForm syn = BeanUtils.copy(commandVo, CommandForm.class);
            iMeterCommandFactory.edit(syn);
            LOGGER.error(e);
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, "下发命令出错", e);
        }
    }

}
