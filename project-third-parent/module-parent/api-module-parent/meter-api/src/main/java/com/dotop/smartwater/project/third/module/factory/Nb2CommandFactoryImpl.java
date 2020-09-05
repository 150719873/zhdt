package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.project.third.module.api.factory.IMeterCommandFactory;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDockingFactory;
import com.dotop.smartwater.project.third.module.api.factory.INb2CommandFactory;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.constants.TxCode;
import com.dotop.smartwater.project.third.module.api.factory.*;
import com.dotop.smartwater.project.third.module.api.service.IMeterDeviceService;
import com.dotop.smartwater.project.third.module.api.service.IWaterDeviceService;
import com.dotop.smartwater.project.third.module.client.IWaterClientService;
import com.dotop.smartwater.project.third.module.core.third.nb2.form.UserSynForm;
import com.dotop.smartwater.project.third.module.core.third.nb2.vo.UserSynVo;
import com.dotop.smartwater.project.third.module.core.utils.Nb2Utils;
import com.dotop.smartwater.project.third.module.core.water.bo.CommandBo;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.third.module.core.water.bo.DockingBo;
import com.dotop.smartwater.project.third.module.core.water.form.CommandForm;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DockingVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.dotop.smartwater.project.third.module.core.constants.DockingConstants.*;

/**
 *
 */
@Component
public class Nb2CommandFactoryImpl implements INb2CommandFactory {

    private final static Logger LOGGER = LogManager.getLogger(Nb2CommandFactoryImpl.class);

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
    public UserSynVo sendCommand(UserSynForm userSynForm) throws FrameworkRuntimeException {
        UserSynVo userSynVo = BeanUtils.copy(userSynForm, UserSynVo.class);
        try {
            DockingVo dockingVo = iMeterDockingFactory.get(Nb2Utils.getUserInfo(userSynForm));
            // 对比中间库设备是否存在
            DeviceBo deviceBo = new DeviceBo();
            deviceBo.setEnterpriseid(dockingVo.getEnterpriseid());
            deviceBo.setDevno(userSynForm.getMeterno());
            DeviceVo deviceVo = iMeterDeviceService.get(deviceBo);
            DeviceVo wDeviceVo = iWaterDeviceService.get(deviceBo);
            if (deviceVo == null) {
                //同步设备
                deviceVo = wDeviceVo;
                // TODO 若deviceVo为空，则水务库数据出现错误
                DeviceBo sync = BeanUtils.copy(deviceVo, DeviceBo.class);
                sync.setId(UuidUtils.getUuid());
                List<DeviceBo> deviceBos = new ArrayList<>();
                deviceBos.add(sync);
                iMeterDeviceService.adds(deviceBos);
            } else if (wDeviceVo != null &&
                    (!wDeviceVo.getDevid().equals(deviceVo.getDevid())
                            || !wDeviceVo.getDeveui().equals(deviceVo.getDeveui()))) {
                deviceVo.setDevid(wDeviceVo.getDevid());
                deviceVo.setDeveui(wDeviceVo.getDeveui());
                DeviceBo sync = BeanUtils.copy(deviceVo, DeviceBo.class);
                List<DeviceBo> deviceBos = new ArrayList<>();
                deviceBos.add(sync);
                iMeterDeviceService.edits(deviceBos);
            }
            //向水务平台发送命令准备
            CommandBo commandBo = new CommandBo();
            if (REMOTE_DEVICE_OPEN.equals(userSynForm.getType())) {
                commandBo.setCommand(TxCode.OpenCommand);
            } else if (REMOTE_DEVICE_CLOSE.equals(userSynForm.getType())) {
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
                } else {
                    throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, "下发命令失败");
                }
            } catch (Exception e) {
                commandVo.setDes(RESULT_FAIL_MSG);
                commandVo.setStatus(COMMAND_FINISH);
                commandVo.setResult(RESULT_FAIL);
                CommandForm syn = BeanUtils.copy(commandVo, CommandForm.class);
                iMeterCommandFactory.edit(syn);
                throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, "下发命令出错");
            }
            userSynVo.setType(REMOTE_SUCCESS);
            return userSynVo;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e, "userSynForm:", userSynForm));
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, e.getMessage(), e);
        }
    }
}
