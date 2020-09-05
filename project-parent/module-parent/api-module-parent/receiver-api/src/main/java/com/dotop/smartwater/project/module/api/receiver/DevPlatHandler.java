package com.dotop.smartwater.project.module.api.receiver;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.dependence.cache.StringListCache;
import com.dotop.smartwater.project.module.service.device.*;
import com.dotop.smartwater.project.module.core.third.vo.pipe.PipePushVo;
import com.dotop.smartwater.project.module.core.water.bo.*;
import com.dotop.smartwater.project.module.core.water.vo.DeviceSubscribeVo;
import com.dotop.smartwater.project.module.service.device.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.third.form.iot.BaseForm;
import com.dotop.smartwater.project.module.core.third.form.iot.DownlinkDataStatusForm;
import com.dotop.smartwater.project.module.core.third.form.iot.EcpextForm;
import com.dotop.smartwater.project.module.core.third.form.iot.MeterInfoForm;
import com.dotop.smartwater.project.module.core.third.form.iot.MeterNb;
import com.dotop.smartwater.project.module.core.third.form.iot.MeterOperForm;
import com.dotop.smartwater.project.module.core.third.form.iot.PushDataForm;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.vo.DeviceDownlinkVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import org.apache.commons.lang3.StringUtils;

/**
 * 开发者平台的推送/单播下发/组播下发/单播下发结果
 *

 * @version 1.0.0
 * @date 2018年4月18日 上午9:24:36
 */
@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
public class DevPlatHandler {

    private final static Logger logger = LogManager.getLogger(DevPlatHandler.class);

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IDeviceUplinkService iDeviceUplinkService;

    @Autowired
    private IDeviceDownlinkService iDeviceDownlinkService;

    @Autowired
    private IDeviceWarningService iDeviceWarningService;

    @Autowired
    private IDeviceSubscribeService iDeviceSubscribeService;

    @Autowired
    private StringListCache stringListCache;

    private static final String QueueName = "pushPipeLeakage";

    /**
     * 接收IOT推送过来的数据
     *
     * @param data    数据
     * @param taptype 阀门类型
     */
    public void pushData(PushDataForm data, Integer taptype) {
        DeviceVo device = iDeviceService.findByDevEUI(data.getDevEui());
        MeterInfoForm meterInfo = data.getMeterInfo();
        MeterOperForm meterOper = data.getMeterOper();
        MeterNb meterNb = data.getMeterNb();
        BaseForm base = data.getBase();
        EcpextForm ecpext = data.getEcpext();
        String water = null;
        Integer tapStatus = null;
        try {
            // 新增设备
            if (device == null) {
                logger.info("设备[ {} ]未在水务平台注册", data.getDevEui());
                return;
            } 
                
            device.setFlag(3);
            if (meterInfo != null) {
            	//验证返回数据是否存在预警信息,如果存在则插入
                exceptionHandle(meterInfo, device);
                
                //封住数据到设备对象中
                device.setBattery(meterInfo.getAbnormalPower() != null ?  Integer.valueOf(meterInfo.getAbnormalPower()) : null);
                device.setAbnormalCurrent(meterInfo.getAbnormalCurrent() != null ? Integer.valueOf(meterInfo.getAbnormalCurrent()) : null);
                device.setTimeSync(meterInfo.getTimeSync()!=null ? Integer.valueOf(meterInfo.getTimeSync()) : null);
                device.setCaliber(meterInfo.getCaliber() != null ? meterInfo.getCaliber() : "");
                
                // 验证阀门状态 0：关闭，1：开启
                if (meterInfo.getValveStatus() != null) {
                    tapStatus = meterInfo.getValveStatus().equals("1") ? 1 : 0;
                    device.setTapstatus(tapStatus);
                }
                
                // 获取读数信息
                if (meterInfo.getWaterConsumption() != null) {
                    water = meterInfo.getWaterConsumption();
                    device.setWater(Double.parseDouble(water));
                }

                // 获取流量计厂家
                if (meterInfo.getFactory() != null) {
                    device.setFactoryCode(meterInfo.getFactory());
                }

                if (meterInfo.getLifeStatus() != null) {
                    device.setLifeStatus(meterInfo.getLifeStatus());
                    switch (meterInfo.getLifeStatus()) {
					case 1:
						device.setStatus(WaterConstants.DEVICE_STATUS_STORAGE);
						device.setExplain("已储存");
						break;
					case 3:
						device.setStatus(WaterConstants.DEVICE_STATUS_SCRAP);
						device.setExplain("已报废");
						break;
					default:
						device.setStatus(WaterConstants.DEVICE_STATUS_ON_USE);
						device.setExplain("在线");
						break;		
					}
                } else {
                	device.setStatus(WaterConstants.DEVICE_STATUS_ON_USE);
                	device.setExplain("在线");
                }
            }
            
            
            
            if (base != null) {
                device.setActdevType(base.getActDevtyp() != null ? base.getActDevtyp() : null);
                device.setVersion(base.getActDevver() != null ? base.getActDevver() : null);
                device.setIccid(base.getIccid() != null ? base.getIccid() : null);
            }

            if (meterNb != null) {
                device.setPci(meterNb.getPci() != null ? meterNb.getPci() : null);
                device.setCellId(meterNb.getCellId() != null ? meterNb.getCellId() : null);
                device.setVer(meterNb.getVer() != null ? meterNb.getVer() : null);
                if (!StringUtils.isEmpty(meterNb.getImsi())) {
                	device.setImsi(meterNb.getImsi());	
                }
            }

            device.setLsnr(data.getLsnr() != null ? Double.parseDouble(data.getLsnr()) : null);
            device.setRssi(data.getRssi() != null ? Long.parseLong(data.getRssi()) : null);
            
            // 兼容旧版
            Long downRssi = null;
            if (ecpext != null) {
                device.setLsnr(ecpext.getLsnr() != null ? Double.parseDouble(ecpext.getLsnr()) : null);
                if (ecpext.getRssi() != null) {
                    device.setRssi(Long.parseLong(data.getRssi()));
                    downRssi = device.getRssi();
                }
                // TODO 后期可能有用
                /*
                 * if (ecpext.getUpCount()!=null) {
                 *
                 * }
                 */
                // NB参数
                if (ecpext.getDownRssi() != null) {
                    device.setRssi(Long.parseLong(ecpext.getDownRssi()));
                    downRssi = device.getRssi();
                }
            }
            
            Double snr = null;
            if (meterNb != null) {
                if (meterNb.getSnr() != null) {
                    device.setLsnr(Double.parseDouble(meterNb.getSnr()));
                    snr = device.getLsnr();
                }
            }

            Date date = new Date();
            if (data.getRxTime() != null) {
                date = DateUtils.parse(data.getRxTime(), DateUtils.YYYY_MM_DD_HH_MM_SS_SSS);
                if (date == null) {
                    date = new Date();
                }
            }

            device.setUplinktime(DateUtils.formatDatetime(date));
            DeviceBo deviceBo = new DeviceBo();
            BeanUtils.copyProperties(device, deviceBo);

            logger.info(LogMsg.to("msg", "更新上级水表读数", "deviceBo", deviceBo));
            // 更新上级水表读数
            
            iDeviceService.updateDeviceWaterV2(deviceBo);

            if (data.getLsnr() != null || data.getRssi() != null
                    || meterInfo != null || meterOper != null || base != null) {
                DeviceUplinkBo deviceUplink = new DeviceUplinkBo();
                deviceUplink.setConfirmed(true);
                deviceUplink.setDeveui(data.getDevEui());
                deviceUplink.setDevid(device.getDevid());
                if (data.getLsnr() != null) {
                    deviceUplink.setLsnr(Double.parseDouble(data.getLsnr()));
                }
                if (snr != null) {
                    deviceUplink.setLsnr(snr);
                }
                if (data.getRssi() != null) {
                    deviceUplink.setRssi(Long.parseLong(data.getRssi()));
                }
                if (downRssi != null) {
                    deviceUplink.setRssi(downRssi);
                }
                deviceUplink.setRxtime(date);
                deviceUplink.setUplinkData(JSONUtils.toJSONString(data));
                deviceUplink.setWater(water);
                deviceUplink.setTapstatus(tapStatus);
                deviceUplink.setDate(DateUtils.format(date, "yyyyMM"));

                if (meterOper != null) {
                    /**
                     * 水表调整保存
                     *
                     * @author chenjiayi
                     * @date 2019-01-24
                     */
                    BeanUtils.copyProperties(meterOper, deviceUplink);
                }
                if (meterInfo != null) {
                    BeanUtils.copyProperties(meterInfo, deviceUplink);
                    if (meterInfo.getLifeStatus() != null) {
                        deviceUplink.setLifeStatus(String.valueOf(meterInfo.getLifeStatus()));
                    }
                }
                if (meterOper != null) {
                    BeanUtils.copyProperties(meterOper, deviceUplink);
                }
                if (meterNb != null) {
                    BeanUtils.copyProperties(meterNb, deviceUplink);
                    deviceUplink.setTxTimeNb(meterNb.getTxTime());
                    deviceUplink.setRxTimeNb(meterNb.getRxTime());
                }
                if (base != null) {
                    BeanUtils.copyProperties(base, deviceUplink);
                }
                iDeviceUplinkService.add(deviceUplink);

                DeviceSubscribeBo bo = new DeviceSubscribeBo();
                bo.setDevno(device.getDevno());
                bo.setEnterpriseid(device.getEnterpriseid());
                DeviceSubscribeVo vo = iDeviceSubscribeService.get(bo);
                if(vo != null){
                    PipePushVo pipePushVo = new PipePushVo();
                    pipePushVo.setForm(deviceUplink);
                    pipePushVo.setDevno(bo.getDevno());
                    pipePushVo.setEnterptiseid(bo.getEnterpriseid());
                    stringListCache.leftPush(QueueName, JSONUtils.toJSONString(pipePushVo));
                }
            }

            logger.info(LogMsg.to("更新上级水表属性", data.getDevEui()));
        } catch (Exception ex) {
            logger.error(ex);
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, BaseExceptionConstants.BASE_ERROR, ex);
        }
    }

    /**
     * 水表异常处理
     *
     * @param info
     */
    private void exceptionHandle(MeterInfoForm info, DeviceVo device) {
        try {
            String ex = "1";
            // 有异常,插入数据库
            if (ex.equals(info.getAbnormalCurrent()) || ex.equals(info.getAbnormalPower())
                    || ex.equals(info.getMagneticAttack()) || ex.equals(info.getOpenException())
                    || ex.equals(info.getCloseException())
                    || ex.equals(info.getAnhydrousAbnormal())
                    || ex.equals(info.getDisconnectionAbnormal())
                    || ex.equals(info.getPressureException())) {
                // 找出最近一条异常是否没处理,没处理则更新
            	DeviceWarningBo deviceWarningBo = new DeviceWarningBo();
                BeanUtils.copyProperties(info, deviceWarningBo);
                deviceWarningBo.setId(UuidUtils.getUuid());
                deviceWarningBo.setDevid(device.getDevid());
                deviceWarningBo.setDevno(device.getDevno());
                deviceWarningBo.setDeveui(device.getDeveui());
                deviceWarningBo.setWarningNum(1);
                deviceWarningBo.setStatus(WaterConstants.DEVICE_WARNING_HANDLE_STATUS_UNTREATED);
                deviceWarningBo.setAddress(device.getUseraddr());
        		deviceWarningBo.setModeId(device.getMode());
        		deviceWarningBo.setModeName(device.getModeName());
                deviceWarningBo.setCtime(new Date());
                deviceWarningBo.setEnterpriseid(device.getEnterpriseid());
                iDeviceWarningService.addDeviceWarning(deviceWarningBo);
            }
        } catch (Exception e) {
            logger.error("exceptionHandle error", e);
        }

    }

    public void handle(DownlinkDataStatusForm downlinkDataStatus) {
        // List<DeviceDownlinkVo> deviceDownlinkList = iDeviceDownlinkService
        // .findByClientIdStr(downlinkDataStatus.getOrderNum());
        DeviceDownlinkBo deviceDownlinkBo = new DeviceDownlinkBo();
        deviceDownlinkBo.setClientid(downlinkDataStatus.getOrderNum());
        List<DeviceDownlinkVo> deviceDownlinkList = iDeviceDownlinkService.findByClientId(deviceDownlinkBo);

        logger.info("select downlink by client_id");
        if (deviceDownlinkList != null && deviceDownlinkList.size() > 0) {
            for (DeviceDownlinkVo deviceDownlink : deviceDownlinkList) {
                // 等待
                if (downlinkDataStatus.getStatus().equals("wait")) {
                    deviceDownlink.setStatus(0);
                    // 成功
                } else if (downlinkDataStatus.getStatus().equals("succ")) {
                    deviceDownlink.setStatus(1);
                    // 失败
                } else if (downlinkDataStatus.getStatus().equals("fail")) {
                    deviceDownlink.setStatus(3);
                }
                deviceDownlink.setTxtime(new Date());
                deviceDownlinkBo = new DeviceDownlinkBo();
                BeanUtils.copyProperties(deviceDownlink, deviceDownlinkBo);
                iDeviceDownlinkService.update(deviceDownlinkBo);
                logger.info("update downlink");
            }
        }
    }
}
