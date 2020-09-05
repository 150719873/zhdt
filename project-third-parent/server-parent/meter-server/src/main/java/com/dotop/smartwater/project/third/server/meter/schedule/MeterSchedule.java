package com.dotop.smartwater.project.third.server.meter.schedule;

import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.third.module.api.factory.IMeterCommandFactory;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDockingFactory;
import com.dotop.smartwater.project.third.module.api.factory.IThirdObtainFactory;
import com.dotop.smartwater.project.third.module.core.utils.VideoUtils;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.vo.DockingVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dotop.smartwater.project.third.module.core.constants.DockingConstants.*;

/**
 *
 */
public class MeterSchedule {

    private static final Logger LOGGER = LogManager.getLogger(MeterSchedule.class);

    /**
     * 每隔多少分钟执行一次定时器
     */
    private static final int RANG = 5;
    /**
     * 用于记录某次操作是否执行完毕
     */
    private Boolean status = true;

    @Autowired
    IMeterDockingFactory iMeterDockingFactory;
    @Resource(name = "NbFactoryImpl")
    IThirdObtainFactory nbFactory;
    @Resource(name = "VideoFactoryImpl")
    IThirdObtainFactory videoFactory;
    @Resource(name = "ZhswFactoryImpl")
    IThirdObtainFactory zhswFactoryImpl;
    @Resource(name = "LoraFactoryImpl")
    IThirdObtainFactory loraFactoryImpl;
    @Autowired
    IMeterCommandFactory iMeterCommandFactory;

    @Scheduled(initialDelay = 5000, fixedRate = RANG * 60 * 1000)
    public void updateTask() {
        LOGGER.info("执行定时任务-----------------------------------------------");
        if (!status) {
            LOGGER.error("上次定时器任务还没执行完, 可能出现异常!");
            return;
        }
        status = false;
        try {
            DockingForm dockingForm = new DockingForm();
            List<DockingVo> dockingVos = iMeterDockingFactory.list(dockingForm);
            List<DockingForm> videoLoginList = new ArrayList<>();
            List<DockingForm> nb1LoginList = new ArrayList<>();
            List<DockingForm> szLoraLoginList = new ArrayList<>();
            DockingForm clearDockingForm = new DockingForm();
            DockingForm getAccessDockingForm = new DockingForm();
            dockingVos.forEach(dockingVo -> {
                if (VIDEO_POST_LOGIN.equals(dockingVo.getType())) {
                    DockingForm docking = BeanUtils.copy(dockingVo, DockingForm.class);
                    videoLoginList.add(docking);
                } else if (VIDEO_GET_CLEANUP.equals(dockingVo.getType())) {
                    clearDockingForm.setId(dockingVo.getId());
                    clearDockingForm.setHost(dockingVo.getHost());
                    clearDockingForm.setUrl(dockingVo.getUrl());
                } else if (DTNB_LOGIN.equals(dockingVo.getType())) {
                    DockingForm docking = BeanUtils.copy(dockingVo, DockingForm.class);
                    nb1LoginList.add(docking);
                } else if (ZHSW_POST_GETACCESSTOKEN.equals(dockingVo.getType())) {
                    getAccessDockingForm.setUrl(dockingVo.getUrl());
                    getAccessDockingForm.setHost(dockingVo.getHost());
                    getAccessDockingForm.setId(dockingVo.getId());
                    getAccessDockingForm.setUsername(dockingVo.getUsername());
                    getAccessDockingForm.setPassword(dockingVo.getPassword());
                } else if (HAT_LORA_LOGIN.equals(dockingVo.getType())) {
                    DockingForm docking = BeanUtils.copy(dockingVo, DockingForm.class);
                    szLoraLoginList.add(docking);
                }
            });
//            LOGGER.info(LogMsg.to("dockingVos", dockingVos));
            DateTime now = new DateTime(DateTime.now());
            int nowMin = now.getMinuteOfDay();
            dockingVos.forEach(dockingVo -> {
                try {
                    if (dockingVo.getRang() != null) {
                        Date date = dockingVo.getTime();
                        DateTime dateTime = new DateTime(date);
                        int min = dateTime.getMinuteOfDay();
                        int between = nowMin - min;
                        // 是否需要执行当前请求
                        LOGGER.info(LogMsg.to("nowMin", nowMin, "min", min, "between", between, "rang", dockingVo.getRang(), "RANG", RANG));
                        LOGGER.info(LogMsg.to("nowMin", between > dockingVo.getRang(), "min", ((between % dockingVo.getRang()) < RANG)));
                        LOGGER.info(LogMsg.to("nowMin", (nowMin == min || (between > dockingVo.getRang() && (between % dockingVo.getRang()) < RANG))));
                        if (nowMin == min || (between > dockingVo.getRang() && (between % dockingVo.getRang()) < RANG)) {
                            DockingForm docking = BeanUtils.copy(dockingVo, DockingForm.class);
                            LOGGER.info(LogMsg.to("dockingVo", dockingVo));
//                            LOGGER.info(LogMsg.to("type", dockingVo.getType()));
                            switch (dockingVo.getType()) {
                                case DTNB_GET_DEVICE_INFO:
                                    nb1LoginList.forEach(loginDocking -> {
                                        if (loginDocking.getEnterpriseid().equals(docking.getEnterpriseid())) {
                                            nbFactory.updateDevice(docking, loginDocking);
                                        }
                                    });
                                    break;
                                case DTNB_GET_DEVICE_CURRENT_DATA:
                                    LOGGER.info(LogMsg.to("nb1LoginList", nb1LoginList));
                                    nb1LoginList.forEach(loginDocking -> {
                                        if (loginDocking.getEnterpriseid().equals(docking.getEnterpriseid())) {
                                            LOGGER.info(LogMsg.to("enterpriseid", loginDocking.getEnterpriseid()));
                                            nbFactory.updateDeviceUplink(docking, loginDocking);
                                        }
                                    });
                                    break;
                                case VIDEO_POST_IMETER:
                                    videoLoginList.forEach(loginDocking -> {
                                        if (loginDocking.getEnterpriseid().equals(docking.getEnterpriseid())) {
                                            videoFactory.updateDevice(docking, loginDocking);
                                        }
                                    });
                                    break;
                                case VIDEO_POST_CUSTOMER:
                                    videoLoginList.forEach(loginDocking -> {
                                        if (loginDocking.getEnterpriseid().equals(docking.getEnterpriseid())) {
                                            if (!CHARGE.equals(loginDocking.getSystem())) {
                                                videoFactory.updateOwner(docking, loginDocking);
                                            }
                                        }
                                    });
                                    break;
                                case VIDEO_POST_RECORD:
                                    videoLoginList.forEach(loginDocking -> {
                                        if (loginDocking.getEnterpriseid().equals(docking.getEnterpriseid())) {
                                            videoFactory.updateDeviceUplink(docking, loginDocking);
                                        }
                                    });
                                    break;
                                case ZHSW_POST_UPDATECLIENTINFO:
                                    zhswFactoryImpl.updateOwner(docking, getAccessDockingForm);
                                    break;
                                case ZHSW_POST_UPDATEWATERMETERRECORD:
                                    zhswFactoryImpl.updateDevice(docking, getAccessDockingForm);
                                    break;
                                case HAT_LORA_DEVICE_INFO:
                                    szLoraLoginList.forEach(loginDocking -> {
                                        if (loginDocking.getEnterpriseid().equals(docking.getEnterpriseid())) {
                                            loraFactoryImpl.updateDevice(docking, loginDocking);
                                        }
                                    });
                                    break;
                                case HAT_LORA_GET_METER_VALUE_BY_ID:
                                    szLoraLoginList.forEach(loginDocking -> {
                                        if (loginDocking.getEnterpriseid().equals(docking.getEnterpriseid())) {
                                            loraFactoryImpl.updateDeviceUplink(docking, loginDocking);
                                        }
                                    });
                                    break;
                                default:
                                    break;
                            }

                        }
                    }
                } catch (Exception e) {
                    LOGGER.error(e);
                    LOGGER.error(LogMsg.to(e), "updateTask");
                }
            });
            // 定时任务结束， 清楚token
            VideoUtils.clearToken(clearDockingForm);
        } catch (Exception e) {
            LOGGER.error(e);
            LOGGER.error(LogMsg.to(e), "updateTask");
        }
        status = true;
        LOGGER.info("定时任务执行完毕------------------------------------");
    }


//    @Scheduled(initialDelay = 5000, fixedRate = 2 * 60 * 1000)
//    public void updateCommand() {
//        CommandForm commandForm = new CommandForm();
//        commandForm.setStatus(COMMAND_READY);
//        List<CommandVo> commandVos = iMeterCommandFactory.list(commandForm);
//        commandVos.forEach(commandVo -> {
//            DateTime lastTime = new DateTime(commandVo.getLastDate());
//
//        });
//    }
}
