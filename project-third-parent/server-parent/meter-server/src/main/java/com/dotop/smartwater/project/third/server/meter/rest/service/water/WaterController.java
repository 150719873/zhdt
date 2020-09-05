package com.dotop.smartwater.project.third.server.meter.rest.service.water;


import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.core.water.constants.ModeConstants;
import com.dotop.smartwater.project.third.module.api.factory.IMeterCommandFactory;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDockingFactory;
import com.dotop.smartwater.project.third.module.api.factory.IThirdObtainFactory;
import com.dotop.smartwater.project.third.module.api.service.IWaterDeviceService;
import com.dotop.smartwater.project.third.module.core.constants.DockingConstants;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.third.module.core.water.form.CommandForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *
 */
@RestController
@RequestMapping("/water")
public class WaterController implements BaseController<BaseForm> {

    private static final Logger LOGGER = LogManager.getLogger(WaterController.class);

    @Resource(name = "NbFactoryImpl")
    IThirdObtainFactory iNbFactory;
    @Resource(name = "LoraFactoryImpl")
    IThirdObtainFactory iLoraFactory;
    @Autowired
    IWaterDeviceService iWaterDeviceService;
    @Autowired
    IMeterCommandFactory iMeterCommandFactory;
    @Autowired
    IMeterDockingFactory iMeterDockingFactory;

    /**
     * 下发命令
     * 1. 水务平台通过调用接口下发
     * 2. 如果设备不存在，返回result，告诉水务平台下发失败
     * 3. 如果设备存在，获取设备信息和通信方式协议，记录下发日志，根据协议直接下发对应第三方接口
     * 4. 如果该设备下发只能缓存一条，要先将旧命令的下发日志记录为取消，第三方取消接口不调用，调用编辑命令结果接口
     * 5. 返回命令id信息以便二次查询
     */
    @PostMapping(value = "/downlink", produces = GlobalContext.PRODUCES, consumes = GlobalContext.PRODUCES)
    public String downlink(@RequestBody CommandForm commandForm, @RequestHeader("enterpriseid") String enterpriseid) {
        LOGGER.info(LogMsg.to("commandForm", commandForm, "enterpriseid", enterpriseid));
        CommandVo commandVo = new CommandVo();
        try {
            VerificationUtils.string("enterpriseid", enterpriseid);
            VerificationUtils.string("devid", commandForm.getDevid());
            VerificationUtils.integer("command", commandForm.getCommand());
            //设备
            DeviceBo deviceBo = new DeviceBo();
            deviceBo.setEnterpriseid(enterpriseid);
            deviceBo.setDevid(commandForm.getDevid());
            DeviceVo deviceVo = iWaterDeviceService.get(deviceBo);
            if (deviceVo == null) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, "设备不存在");
            }
            commandForm.setEnterpriseid(enterpriseid);
            commandForm.setDevid(deviceBo.getDevid());
            // 判断通信类型
            String[] modes = deviceVo.getMode().split(",");
            DockingForm login, search, loginForm, typeForm;
            switch (modes[modes.length - 1]) {
                case ModeConstants.HAT_NB:
                    // 华奥通NB登录账户
                    login = new DockingForm();
                    login.setType(DockingConstants.DTNB_LOGIN);
                    login.setEnterpriseid(enterpriseid);
                    loginForm = BeanUtils.copy(iMeterDockingFactory.get(login), DockingForm.class);
                    // 华奥通NB下发命令
                    search = new DockingForm();
                    search.setType(DockingConstants.DTNB_SEND_COMMAND);
                    search.setEnterpriseid(enterpriseid);
                    typeForm = BeanUtils.copy(iMeterDockingFactory.get(search), DockingForm.class);
                    //华奥通NB下发命令
                    commandVo = iNbFactory.sendCommand(typeForm, loginForm, commandForm);
                    break;
                case ModeConstants.HAT_LORA:
                    // 华奥通lora录账户
                    login = new DockingForm();
                    login.setType(DockingConstants.HAT_LORA_LOGIN);
                    login.setEnterpriseid(enterpriseid);
                    loginForm = BeanUtils.copy(iMeterDockingFactory.get(login), DockingForm.class);
                    // 华奥通lora下发命令
                    search = new DockingForm();
                    search.setType(DockingConstants.HAT_LORA_SET_METER_VALUE);
                    // TODO
                    search.setEnterpriseid(enterpriseid);
                    typeForm = BeanUtils.copy(iMeterDockingFactory.get(search), DockingForm.class);
                    // 华奥通lora下发命令
                    if (loginForm == null || typeForm == null) {
                        throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, "无该设备的命令协议");
                    }
                    commandVo = iLoraFactory.sendCommand(typeForm, loginForm, commandForm);
                    break;
                default:
                    throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, "无该设备的命令协议");
            }
            return resp(DockingConstants.RESULT_SUCCESS, DockingConstants.RESULT_SUCCESS_MSG, commandVo);
        } catch (Exception e) {
            LOGGER.error(LogMsg.to("commandForm", commandForm, "enterpriseid", enterpriseid));
            LOGGER.error(LogMsg.to(e.getMessage(), e));
            return resp(DockingConstants.RESULT_FAIL, DockingConstants.RESULT_FAIL_MSG, commandVo);
        }
    }

    /**
     * 取消命令
     * 1. 水务平台通过调用接口取消
     * 2. 如果设备或命令id不存在，返回result，告诉水务平台取消失败
     * 3. 如果命令id存在但是设备不存在，返回result，告诉水务平台取消失败并修改下发日志为取消，调用第三方取消接口，调用编辑命令结果接口
     * 4. 如果都存在，返回result，告诉水务平台取消失败并修改下发日志为取消，调用第三方取消接口，调用编辑命令结果接口
     */
    @PostMapping(value = "/downlink/cancel", produces = GlobalContext.PRODUCES)
    public String downlinkCancel(@RequestBody CommandForm commandForm, @RequestHeader("enterpriseid") String enterpriseid) {
        LOGGER.info(LogMsg.to("commandForm", commandForm, "enterpriseid", enterpriseid));
        CommandVo commandVo = new CommandVo();
        try {
            VerificationUtils.string("clientid", commandForm.getClientid());
            VerificationUtils.string("enterpriseid", enterpriseid);
            //查询出对应的命令记录
            commandForm.setEnterpriseid(enterpriseid);
            CommandVo commandVo1 = iMeterCommandFactory.get(commandForm);
            if (commandVo1 == null) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, "任务不存在");
            }
            DeviceBo deviceBo = new DeviceBo();
            deviceBo.setEnterpriseid(enterpriseid);
            deviceBo.setDevid(commandVo1.getDevid());
            DeviceVo deviceVo = iWaterDeviceService.get(deviceBo);
            DockingForm login = new DockingForm();
            login.setType(DockingConstants.DTNB_LOGIN);
            login.setEnterpriseid(enterpriseid);
            DockingForm loginForm = BeanUtils.copy(iMeterDockingFactory.get(login), DockingForm.class);
            DockingForm search = new DockingForm();
            search.setType(DockingConstants.DTNB_SEND_COMMAND);
            DockingForm typeForm = BeanUtils.copy(iMeterDockingFactory.get(search), DockingForm.class);
            if (deviceVo == null) {
                throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, "设备不存在");
            }
            String[] modes = deviceVo.getMode().split(",");
            switch (modes[modes.length - 1]) {
                case ModeConstants.HAT_NB:
                    commandVo = iNbFactory.cancelCommand(typeForm, loginForm, commandForm);
                    break;
                default:
                    throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, "无该设备的命令协议");
            }
            return resp(DockingConstants.RESULT_SUCCESS, DockingConstants.RESULT_SUCCESS_MSG, commandVo);
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e.getMessage(), e));
            return resp(DockingConstants.RESULT_FAIL, DockingConstants.RESULT_FAIL_MSG, commandVo);
        }
    }

    /**
     * 下发查询结果
     * 1. 水务平台通过接口查询结果
     * 2. 如果设备或命令id不存在，返回result，告诉水务平台取消失败
     * 3. 如果命令id存在，查询本地数据库的下发命令结果，并返回下发查询结果
     */
    @PostMapping(value = "/downlink/result", produces = GlobalContext.PRODUCES)
    public String downlinkResult(@RequestBody CommandForm commandForm, @RequestHeader("enterpriseid") String enterpriseid) {
        LOGGER.info(LogMsg.to(commandForm, commandForm, "enterpriseid", enterpriseid));
        CommandVo commandVo = new CommandVo();
        try {
            VerificationUtils.string("enterpriseid", enterpriseid);
            VerificationUtils.string("clientid", commandForm.getClientid());
            VerificationUtils.string("devid", commandForm.getDevid());
            commandVo = iMeterCommandFactory.get(commandForm);
            return resp(DockingConstants.RESULT_SUCCESS, DockingConstants.RESULT_SUCCESS_MSG, commandVo);
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e.getMessage(), e));
            return resp(DockingConstants.RESULT_FAIL, DockingConstants.RESULT_FAIL_MSG, commandVo);
        }
    }

}
