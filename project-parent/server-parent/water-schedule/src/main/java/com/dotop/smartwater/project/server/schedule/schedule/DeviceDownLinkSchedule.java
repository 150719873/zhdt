package com.dotop.smartwater.project.server.schedule.schedule;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.DeviceDownlinkBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceWarningBo;
import com.dotop.smartwater.project.module.core.water.constants.ModeConstants;
import com.dotop.smartwater.project.module.core.water.constants.TxCode;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceWarningVo;
import com.dotop.smartwater.project.module.service.device.IDeviceDownlinkService;
import com.dotop.smartwater.project.module.service.device.IDeviceService;
import com.dotop.smartwater.project.module.service.device.IDeviceWarningService;

/**
 * 自动检测设备下發命令超時
 *

 * @date 2019年8月19日
 */
public class DeviceDownLinkSchedule {

    private static final Logger LOGGER = LogManager.getLogger(DeviceDownLinkSchedule.class);

    @Autowired
    private IDeviceDownlinkService iDeviceDownlinkService;
    @Autowired
    private IDeviceWarningService iDeviceWarningService;
    @Autowired
    private IDeviceService iDeviceService;

    @Scheduled(initialDelay = 10000, fixedRate = 300000)
    public void init() {
        LOGGER.info("开始获取所有下发中的命令.....");
        List<DeviceDownlinkVo> list;
        DeviceDownlinkBo deviceDownlinkBo = new DeviceDownlinkBo();
        deviceDownlinkBo.setStatus(0);
        list = iDeviceDownlinkService.list(deviceDownlinkBo);
        LOGGER.info("共[" + list.size() + "]个下发中的命令...");
        if (list.size() > 0) {
            for (DeviceDownlinkVo vo : list) {
                if (vo.getMode() != null) {
                    String mode = vo.getMode();
                    switch (mode) {
                        //lora超时时间(120秒)
                        case ModeConstants.DX_LORA:
                            int seconds = DateUtils.secondsBetween(vo.getGentime(), new Date());
                            if (seconds > 120) {
                                handleTimeOut(vo);
                            }
                            break;
                        //Nb超时时间(22小时)
                        case ModeConstants.DX_NB_YD:
                        case ModeConstants.DX_NB_DX:
                        case ModeConstants.DX_NB_LT:
                            int hours = DateUtils.hoursBetween(vo.getGentime(), new Date());
                            if (hours > 22) {
                                handleTimeOut(vo);
                            }
                            break;
                        default:
                            int hs = DateUtils.hoursBetween(vo.getGentime(), new Date());
                            if (hs > 22) {
                                handleTimeOut(vo);
                            }
                            break;
                    }
                }
            }
        } else {
            LOGGER.info("无下发中的命令，结束定时任务");
        }
    }

    private void handleTimeOut(DeviceDownlinkVo vo) {
        DeviceDownlinkBo deviceDownlinkBo = new DeviceDownlinkBo();
        deviceDownlinkBo.setId(vo.getId());
        deviceDownlinkBo.setStatus(3);
        iDeviceDownlinkService.update(deviceDownlinkBo);
        if (vo.getDownlinkdata() != null) {
//            List<String> warnTypeList = new ArrayList<>();
            DeviceWarningBo deviceWarningBo = new DeviceWarningBo();
            switch (Integer.parseInt(vo.getDownlinkdata())) {
                case TxCode.OpenCommand:
                	//1为异常
                	deviceWarningBo.setOpenException("1");
//                    warnTypeList.add("开到位异常");
                    break;
                case TxCode.CloseCommand:
                	deviceWarningBo.setCloseException("1");
//                    warnTypeList.add("关到位异常");
                    break;
                default:
                    String command = TxCode.TxCodeMap.get(Integer.parseInt(vo.getDownlinkdata()));
                    LOGGER.info("[{}]命令超时了", command);
                    return;
                /*case TxCode.GetWaterCommand:
                    warnTypeList.add("关到位异常");
                    warningBo.setDescription("关阀超时");
                    warningBo.setWarningType(warnTypeList);
                    break;*/
            }
            //计算告警次数
            Integer warningNum = 1;
            deviceWarningBo.setId(UuidUtils.getUuid());
            deviceWarningBo.setStatus(DeviceWarningVo.DEVICE_WARNING_STATUS_WAIT);
            deviceWarningBo.setWarningNum(warningNum);
            deviceWarningBo.setCtime(new Date());
            
            if(StringUtils.isNotBlank(vo.getDeveui())) {
            	DeviceVo deviceVo = iDeviceService.findByDevEUI(vo.getDeveui());
                deviceWarningBo.setDevid(deviceVo.getDevid());
                deviceWarningBo.setDevno(deviceVo.getDevno());
                deviceWarningBo.setDeveui(deviceVo.getDeveui());
                deviceWarningBo.setModeId(deviceVo.getMode());
                deviceWarningBo.setModeName(deviceVo.getModeName());
                deviceWarningBo.setAddress(deviceVo.getUseraddr());
                deviceWarningBo.setEnterpriseid(deviceVo.getEnterpriseid());
            }else {
                deviceWarningBo.setDevid(vo.getDevid());
                deviceWarningBo.setDevno(vo.getDevno());
                deviceWarningBo.setDeveui(vo.getDeveui());
                deviceWarningBo.setModeId(vo.getMode());
                deviceWarningBo.setModeName(vo.getModeName());
                deviceWarningBo.setEnterpriseid(vo.getEnterpriseid());
            }
            iDeviceWarningService.addDeviceWarning(deviceWarningBo);
        }
    }
}
