package com.dotop.smartwater.project.third.server.meter.schedule;

import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.core.water.constants.ModeConstants;
import com.dotop.smartwater.project.module.core.water.constants.TxCode;
import com.dotop.smartwater.project.third.module.api.factory.IMeterCommandFactory;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDockingFactory;
import com.dotop.smartwater.project.third.module.api.factory.IThirdObtainFactory;
import com.dotop.smartwater.project.third.module.api.service.IWaterCommandService;
import com.dotop.smartwater.project.third.module.api.service.IWaterDeviceService;
import com.dotop.smartwater.project.third.module.core.constants.DockingConstants;
import com.dotop.smartwater.project.third.module.core.water.bo.CommandBo;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.third.module.core.water.form.CommandForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class CommandSchedule {

    private static final Logger LOGGER = LogManager.getLogger(CommandSchedule.class);

    /**
     * 每隔多少分钟执行一次定时器
     */
    private static final int RANG = 5;
    /**
     * 超过多少个小时便不处理的任务
     */
    private static final int TIMEOUT = 24;

    @Autowired
    IMeterCommandFactory iMeterCommandFactory;
    @Autowired
    IMeterDockingFactory iMeterDockingFactory;
    @Autowired
    IWaterDeviceService iWaterDeviceService;
    @Resource(name = "NbFactoryImpl")
    IThirdObtainFactory iNbFactory;
    @Autowired
    IWaterCommandService iWaterCommandService;

    /**
     * 定时扫描下发命令获取结果
     * 1. 根据命令id和设备通信协议，调用对应的第三方接口或水务平台数据库获取命令结果
     * 2. 如果一个设备的同一个命令，需要记录取消(下发命令时需要处理)
     * 3. 获取命令结果后推送回水务平台或修改中间库记录
     */
    @Scheduled(initialDelay = 5000, fixedRate = RANG * 60 * 1000)
    public void task() {
        LOGGER.info("task:开始执行命令状态更新定时器-------------------------");
        try {
            CommandForm commandForm = new CommandForm();
            commandForm.setStatus(DockingConstants.COMMAND_DOING);
            List<CommandVo> commandVos = iMeterCommandFactory.list(commandForm);
            Date now = new Date();
            commandVos.forEach(commandVo -> {
                try {
                    //筛选只处理最近的任务,超过时限默认失败
                    int i = DateUtils.hoursBetween(commandVo.getCreateDate(), now);
                    if (i <= TIMEOUT && StringUtils.isNotBlank(commandVo.getClientid())) {
                        DeviceBo deviceBo = new DeviceBo();
                        deviceBo.setEnterpriseid(commandVo.getEnterpriseid());
                        deviceBo.setDevid(commandVo.getDevid());
                        DeviceVo deviceVo = iWaterDeviceService.get(deviceBo);
                        if (deviceVo != null) {
                            String[] modes = deviceVo.getMode().split(",");
                            switch (modes[modes.length - 1]) {
                                case ModeConstants.HAT_NB:
                                    // TODO 每个执行命令都要获取登录信息
                                    DockingForm login = new DockingForm();
                                    login.setType(DockingConstants.DTNB_LOGIN);
                                    login.setEnterpriseid(commandVo.getEnterpriseid());
                                    DockingForm loginForm = BeanUtils.copy(iMeterDockingFactory.get(login), DockingForm.class);
                                    // TODO 每个执行命令都要获取登录信息
                                    DockingForm search = new DockingForm();
                                    search.setType(DockingConstants.DTNB_GET_COMMAND_STATUS);
                                    search.setEnterpriseid(commandVo.getEnterpriseid());
                                    DockingForm typeForm = BeanUtils.copy(iMeterDockingFactory.get(search), DockingForm.class);
                                    // 执行调用
                                    iNbFactory.updateCommandStatus(typeForm, loginForm, BeanUtils.copy(commandVo, CommandForm.class));
                                    break;
                                case ModeConstants.DX_LORA:
                                case ModeConstants.DX_NB_DX:
                                case ModeConstants.DX_NB_YD:
                                    CommandBo commandBo = new CommandBo();
                                    commandBo.setClientid(commandVo.getClientid());
                                    commandBo.setDevid(commandVo.getDevid());
                                    commandBo.setEnterpriseid(commandVo.getEnterpriseid());
                                    CommandVo update = iWaterCommandService.get(commandBo);
                                    if (TxCode.DOWNLINK_STATUS_SUCCESS == update.getStatus()) {
                                        commandVo.setResult(DockingConstants.RESULT_SUCCESS);
                                        commandVo.setStatus(DockingConstants.COMMAND_FINISH);
                                        commandVo.setDes(DockingConstants.RESULT_SUCCESS_MSG);
                                        iMeterCommandFactory.edit(BeanUtils.copy(commandVo, CommandForm.class));
                                    } else if (TxCode.DOWNLINK_STATUS_FAIL == update.getStatus()) {
                                        commandVo.setResult(DockingConstants.RESULT_FAIL);
                                        commandVo.setStatus(DockingConstants.COMMAND_FINISH);
                                        commandVo.setDes(DockingConstants.RESULT_FAIL_MSG);
                                        iMeterCommandFactory.edit(BeanUtils.copy(commandVo, CommandForm.class));
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    } else {
                        commandVo.setResult(DockingConstants.RESULT_FAIL);
                        commandVo.setStatus(DockingConstants.COMMAND_FINISH);
                        commandVo.setDes(DockingConstants.RESULT_FAIL_MSG);
                        iMeterCommandFactory.edit(BeanUtils.copy(commandVo, CommandForm.class));
                    }
                } catch (Exception e) {
                    LOGGER.error(LogMsg.to("commandVo", commandVo, e));
                }
            });
        } catch (Exception e) {
            LOGGER.error(e);
            LOGGER.error(LogMsg.to("查询出现错误", e));
        }
        LOGGER.info("task:命令状态更新完毕-------------------------");
    }
}
