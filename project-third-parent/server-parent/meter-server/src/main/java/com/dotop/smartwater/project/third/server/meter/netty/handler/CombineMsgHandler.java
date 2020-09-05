package com.dotop.smartwater.project.third.server.meter.netty.handler;

import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDockingFactory;
import com.dotop.smartwater.project.third.module.api.factory.kbl.IKblDeviceFactory;
import com.dotop.smartwater.project.third.module.api.service.IWaterDeviceService;
import com.dotop.smartwater.project.third.module.core.constants.DockingConstants;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DockingVo;
import com.dotop.smartwater.project.third.server.meter.core.NumberUtils;
import com.dotop.smartwater.project.third.server.meter.core.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * a55ae9b00000000000000059000f4242000000007794f2d70000001200000000000000120000001201000000128986044119194001784686816304000809300e660002010b088e7a9fffa5fffbffaa00130000000400000001000000000000000005a06400de7c
 * a55ae9b00000000000000059000f42427867B7177794f2d70000001200000000000000120000001201000000128986044119194001784686816304000809300e660002010b088e7a9fffa5fffbffaa00130000000400000001000000000000000005a06400BFA8
 */
public class CombineMsgHandler extends MsgHandler {

    private static final Logger logger = LogManager.getLogger(CombineMsgHandler.class);

    @Value("${combine.file.path}")
    private String filePath;

    public static final String FILE_KEY = "file-bin:content";
    private static final String MSG_HEAD = "A55AE9B0";
//    private static final boolean FLAG_CRC = false;

    @Autowired
    private StringValueCache svc;

    @Autowired
    private IMeterDockingFactory iMeterDockingFactory;

    @Autowired
    private IKblDeviceFactory iKblDeviceFactory;

    @Autowired
    private IWaterDeviceService iWaterDeviceService;

    public void handle(ChannelHandlerContext ctx, String msg) {
        try {
//            logger.info("msg:{}", msg);
            logger.info("开始校验数据----------------------");
            JSONObjects jsonObjects = JSONUtils.parseObject(msg);
            String dataHex = jsonObjects.getString("data");
            dataHex = dataHex.replaceAll("\\s*", "");
//            logger.info(LogMsg.to("dataHex", dataHex));

            // 校验
            CombineModel combineModel = crc(ctx, dataHex);
            if (combineModel == null || !combineModel.isIfHead() || !combineModel.isIfContentHead()) {
                logger.info(LogMsg.to("msg", "该水表校验不通过，中断"));
                ctx.close();
                return;
            }
            if (!combineModel.isIfCrc()) {
                logger.info(LogMsg.to("msg", "该水表crc校验不通过，不中断", "combineModel", JSONUtils.toJSONString(combineModel)));
                return;
            }

            // 内容
            content(combineModel);
            // INSERT INTO `dictionary_child` (`child_id`, `dictionary_id`, `child_name`, `child_value`, `remark`, `create_by`, `create_date`, `last_by`, `last_date`, `is_del`) VALUES ('28,300001,11', '28,300001', '康宝莱', '11', '', '1', '2019-10-12 16:59:38', '1', '2019-10-12 16:59:38', '0');
            // 水务设备处理
            if (combineModel != null && combineModel.isIfHead() && combineModel.isIfLength() && combineModel.isIfContentHead() && combineModel.isIfContent() && combineModel.isIfCrc()) {
                try {
                    water(combineModel);
                } catch (Exception e) {
                    logger.error(LogMsg.to("ctx", ctx, "Exception", e));
                }
            } else {
                logger.info(LogMsg.to("msg", "该水表校验不通过，不执行读数更新或更新程序", "combineModel", JSONUtils.toJSONString(combineModel)));
            }
            // 如果此版本则升级
            // if ("1000004".equalsIgnoreCase(combineModel.getVersion())) {
            // 响应
            response(ctx, combineModel);
            // } else {
            // response2(ctx, combineModel);
            // }
            logger.info("更新抄表数据完成------------");
        } catch (Exception e) {
            logger.error(LogMsg.to("ctx", ctx, "Exception", e));
            ctx.close();
        }
    }

    /**
     * 校验
     */
    private CombineModel crc(ChannelHandlerContext ctx, String dataHex) throws FrameworkRuntimeException {
        CombineModel combineModel = new CombineModel();
        // 同步头校验
        if (StringUtils.isBlank(dataHex) || dataHex.length() < 8) {
            logger.info(LogMsg.to("msg", "校验头部失败"));
            // ctx.close();
            combineModel.setIfHead(false);
            return combineModel;
        }
        String headHex = dataHex.substring(0, 8);
        if (!MSG_HEAD.equalsIgnoreCase(headHex)) {
            logger.info(LogMsg.to("msg", "校验头部失败"));
            // ctx.close();
            combineModel.setIfHead(false);
            return combineModel;
        }
        // 校验头部成功
        combineModel.setIfHead(true);

        // 总共帧数
        String totalHex = dataHex.substring(8, 12);
        // 主站发送的当前帧
        String masterHex = dataHex.substring(12, 16);
        // 从站需要的当前帧
        String slaveHex = dataHex.substring(16, 20);
        // 数据帧内容长度
        String lenHex = dataHex.substring(20, 24);
        int len = NumberUtils.hexToTen(lenHex);
        // 数据帧内容
        if (dataHex.length() <= (24 + len * 2)) {
            logger.info(LogMsg.to("msg", "校验长度失败"));
            // ctx.close();
            combineModel.setIfLength(false);
            // return combineModel;
        } else {
            combineModel.setIfLength(true);
        }
        String contentHex = null;
        if (combineModel.isIfLength()) {
            contentHex = dataHex.substring(24, 24 + len * 2);
            List<String> contentHexs = StringUtils.getStrList(contentHex, 2);
            List<Integer> contents = NumberUtils.hexToTen(contentHexs);
            // 数据帧内容校验
            int crc16 = crc16(contents, contents.size());
            // CRC校验
            String crcHex = dataHex.substring(24 + len * 2);
            int crc = NumberUtils.hexToTen(crcHex);
            logger.info(LogMsg.to("totalHex", totalHex, "masterHex", masterHex, "slaveHex", slaveHex, "lenHex", lenHex, "len", len, "contentHex", contentHex, "crcHex", crcHex, "crc", crc, "crc16", crc16));
            if (crc != crc16) { // TODO XXX
                logger.info(LogMsg.to("msg", "校验CRC失败"));
                // ctx.close();
                combineModel.setIfCrc(false);
                // return combineModel;
            } else {
                combineModel.setIfCrc(true);
            }
        } else {
            combineModel.setIfCrc(false);
            if (dataHex.length() >= 28) {
                contentHex = dataHex.substring(24, dataHex.length() - 4);
            }
        }
        // 校验内容头部
        if (contentHex == null || contentHex.length() < 24) {
            logger.info(LogMsg.to("msg", "校验内容头部失败"));
            combineModel.setIfContentHead(false);
        } else {
            combineModel.setIfContentHead(true);
        }
        // 校验内容
        if (contentHex == null || contentHex.length() != 178) {
            logger.info(LogMsg.to("msg", "校验内容失败或者更新升级"));
            combineModel.setIfContent(false);
        } else {
            combineModel.setIfContent(true);
        }

        // 返回解析模型
        combineModel.setTotalHex(totalHex);
        combineModel.setMasterHex(masterHex);
        combineModel.setSlaveHex(slaveHex);
        combineModel.setLenHex(lenHex);
        combineModel.setContentHex(contentHex);
        return combineModel;
    }


    /**
     * 数据帧内容解析
     */
    private CombineModel content(CombineModel combineModel) throws FrameworkRuntimeException {
        if (!combineModel.isIfContentHead()) {
            logger.info(LogMsg.to("msg", "校验内容头部失败"));
            return combineModel;
        }
        String contentHex = combineModel.getContentHex();
        // 数据帧内容解析
        // 软件版本号
        String versionHex = contentHex.substring(0, 8);
        String version = NumberUtils.hexToTenStr(versionHex);
        // 序列号(装置号区域1)
        String devno1Hex = contentHex.substring(8, 16);
        String devno1 = NumberUtils.hexToTenStr(devno1Hex);
        // 装置号(装置号区域2)
        String devno2Hex = contentHex.substring(16, 24);
        String devno2 = NumberUtils.hexToTenStr(devno2Hex);
        // 组装基础
        combineModel.setVersionHex(versionHex);
        combineModel.setVersion(version);
        combineModel.setDevno1Hex(devno1Hex);
        combineModel.setDevno1(devno1);
        combineModel.setDevno2Hex(devno2Hex);
        combineModel.setDevno2(devno2);
        // TODO 需要判断长度和软件版本号
        String slaveHex = combineModel.getSlaveHex();
        if (!"0000".equalsIgnoreCase(slaveHex)) {
            logger.info(LogMsg.to("versionHex", versionHex, "devno1Hex", devno1Hex, "devno2Hex", devno2Hex));
            logger.info(LogMsg.to("version", version, "devno1", devno1, "devno2", devno2));
            // 如果不是第0帧,不执行内容解析
            return combineModel;
        }
        if (!combineModel.isIfContent()) {
//        if (!combineModel.isIfLength() || !combineModel.isIfCrc()) {
            logger.info(LogMsg.to("msg", "校验内容失败", "versionHex", versionHex, "devno1Hex", devno1Hex, "devno2Hex", devno2Hex, "version", version, "devno1", devno1, "devno2", devno2));
            return combineModel;
        }
        // 正累计
        String waterHex = contentHex.substring(24, 32);
        String water = NumberUtils.hexToTenStr(waterHex);
//        water = new BigDecimal(water).divide(new BigDecimal(100)).toString();
        // 负累计
        String nwaterHex = contentHex.substring(32, 40);
        String nwater = NumberUtils.hexToTenStr(nwaterHex);
        // 霍尔1计数
        String hall1Hex = contentHex.substring(40, 48);
        String hall1 = NumberUtils.hexToTenStr(hall1Hex);
        // 霍尔2计数
        String hall2Hex = contentHex.substring(48, 56);
        String hall2 = NumberUtils.hexToTenStr(hall2Hex);

        // 1000009版本
        // 水表计量单位
        String unitHex = contentHex.substring(56, 58);
        String unit = NumberUtils.hexToTenStr(unitHex);
        // 水表行数
        String waterConsumptionHex = contentHex.substring(58, 66);
//        String waterConsumption = NumberUtils.hexToTenStr(waterConsumptionHex);
        double waterConsumption = (new BigDecimal(NumberUtils.hexToTen(waterConsumptionHex)).divide(new BigDecimal(1000))).doubleValue();

        // SIM卡CCID
        String ccidHex = contentHex.substring(66, 86);
        String ccid = ccidHex;
        // IMEI号
        String imeiHex = contentHex.substring(86, 102);
        String imei = imeiHex.substring(0, 15);

        // 1000009版本
        // earfcn
        String earfcnHex = contentHex.substring(102, 106);
        String earfcn = NumberUtils.hexToTenStr(earfcnHex);
        // EARFCN_OFFSET
        String earfcnOffsetHex = contentHex.substring(106, 110);
        String earfcnOffset = NumberUtils.hexToTenStr(earfcnOffsetHex);
        // PCI
        String pciHex = contentHex.substring(110, 114);
        String pci = NumberUtils.hexToTenStr(pciHex);
        // CELID
        String celidHex = contentHex.substring(114, 122);
//        String celid = NumberUtils.hexToTenStr(celidHex);
        String celid = celidHex;
        // RSRP
        String rsrpHex = contentHex.substring(122, 126);
        short rsrp = (short) NumberUtils.hexToTen(rsrpHex);
        // RSRQ
        String rsrqHex = contentHex.substring(126, 130);
        short rsrq = (short) NumberUtils.hexToTen(rsrqHex);
        // RSSI
        String rssiHex = contentHex.substring(130, 134);
        short rssi = (short) NumberUtils.hexToTen(rssiHex);
        // SNR
        String snrHex = contentHex.substring(134, 138);
        short snr = (short) NumberUtils.hexToTen(snrHex);
        // 复位次数
        String resetTimesHex = contentHex.substring(138, 146);
        String resetTimes = NumberUtils.hexToTenStr(resetTimesHex);
        // NB模块开启次数
        String openNBTimesHex = contentHex.substring(146, 154);
        String openNBTimes = NumberUtils.hexToTenStr(openNBTimesHex);
        // 后台连接成功测试
        String sbctHex = contentHex.substring(154, 162);
        String sbct = NumberUtils.hexToTenStr(sbctHex);
        // 水表底数
        String waterMeterBaseHex = contentHex.substring(162, 170);
//        String waterMeterBase = NumberUtils.hexToTenStr(waterMeterBaseHex);
        double waterMeterBase = (new BigDecimal(NumberUtils.hexToTen(waterMeterBaseHex)).divide(new BigDecimal(1000))).doubleValue();
        // NB上送周期
        String uploadNBCycleHex = contentHex.substring(170, 174);
        String uploadNBCycle = NumberUtils.hexToTenStr(uploadNBCycleHex);
        // 电池电压
        String batteryVoltageHex = contentHex.substring(174, 176);
        String batteryVoltage = NumberUtils.hexToTenStr(batteryVoltageHex);
        // 磁暴标志
        String magneticStormHex = contentHex.substring(176, 178);
        String magneticStorm = NumberUtils.hexToTenStr(magneticStormHex);

        logger.info(LogMsg.to("versionHex", versionHex, "devno1Hex", devno1Hex, "devno2Hex", devno2Hex, "waterHex", waterHex, "nwaterHex", nwaterHex, "hall1Hex", hall1Hex, "hall2Hex", hall2Hex, "ccidHex", ccidHex, "imeiHex", imeiHex, "unitHex", unitHex, "waterConsumptionHex", waterConsumptionHex, "earfcnHex", earfcnHex, "earfcnOffsetHex", earfcnOffsetHex, "pciHex", pciHex, "celidHex", celidHex, "rsrpHex", rsrpHex, "rsrqHex", rsrqHex, "rssiHex", rssiHex, "snrHex", snrHex, "resetTimesHex", resetTimesHex, "openNBTimesHex", openNBTimesHex, "sbctHex", sbctHex, "waterMeterBaseHex", waterMeterBaseHex, "uploadNBCycleHex", uploadNBCycleHex, "batteryVoltageHex", batteryVoltageHex, "magneticStormHex", magneticStormHex));
        logger.info(LogMsg.to("version", version, "devno1", devno1, "devno2", devno2, "water", water, "nwater", nwater, "hall1", hall1, "hall2", hall2, "ccid", ccid, "imei", imei, "unit", unit, "waterConsumption", waterConsumption, "earfcn", earfcn, "earfcnOffset", earfcnOffset, "pci", pci, "celid", celid, "rsrp", rsrp, "rsrq", rsrq, "rssi", rssi, "snr", snr, "resetTimes", resetTimes, "openNBTimes", openNBTimes, "sbct", sbct, "waterMeterBase", waterMeterBase, "uploadNBCycle", uploadNBCycle, "batteryVoltage", batteryVoltage, "magneticStorm", magneticStorm));
        // 返回解析模型
        combineModel.setWaterHex(waterHex);
        combineModel.setWater(water);
        combineModel.setNWaterHex(nwaterHex);
        combineModel.setNWater(nwater);
        combineModel.setHall1Hex(hall1Hex);
        combineModel.setHall1(hall1);
        combineModel.setHall2Hex(hall2Hex);
        combineModel.setHall2(hall2);
        combineModel.setCcidHex(ccidHex);
        combineModel.setCcid(ccid);
        combineModel.setImeiHex(imeiHex);
        combineModel.setImei(imei);
        //1000009
        // earfcn
        combineModel.setEarfcn(earfcn);
        // EARFCN_OFFSET
        combineModel.setEarfcnOffset(earfcnOffset);
        // PCI
        combineModel.setPci(pci);
        // CELID
        combineModel.setCelid(celid);
        // RSRP
        combineModel.setRsrp(String.valueOf(rsrp));
        // RSRQ
        combineModel.setRsrq(String.valueOf(rsrq));
        // RSSI
        combineModel.setRssi(String.valueOf(rssi));
        // SNR
        combineModel.setSnr(String.valueOf(snr));
        // 水表计量单位
        combineModel.setUnit(unit);
        // 水表行数
        combineModel.setWaterConsumption(String.valueOf(waterConsumption));
        // 复位次数
        combineModel.setResetTimes(resetTimes);
        // NB模块开启次数
        combineModel.setOpenNBTimes(openNBTimes);
        // 后台连接成功测试
        combineModel.setSbct(sbct);
        // 水表底数
        combineModel.setWaterMeterBase(String.valueOf(waterMeterBase));
        // NB上送周期
        combineModel.setUploadNBCycle(uploadNBCycle);
        // 电池电压
        combineModel.setBatteryVoltage(batteryVoltage);
        // 磁暴标志
        combineModel.setMagneticStorm(magneticStorm);
        return combineModel;
    }

    /**
     * 水务设备处理
     */
    private void water(CombineModel combineModel) throws FrameworkRuntimeException {
        String slaveHex = combineModel.getSlaveHex();
        if (!"0000".equalsIgnoreCase(slaveHex)) {
            // 如果不是第0帧,不执行读数更新
            return;
        }
        // 平台预先设置
        DeviceBo deviceBo = new DeviceBo();
//        deviceBo.setDevno(combineModel.getDevno1() + "A" + combineModel.getDevno2());
        deviceBo.setDevno(combineModel.getDevno1());
        deviceBo.setDeveui(combineModel.getImei());
        // TODO XXX
//        deviceBo.setDevno("20190407200415");
//        deviceBo.setDeveui("860705044123704");
        List<DeviceVo> list = iWaterDeviceService.list(deviceBo);
        if (list == null || list.size() != 1) {
            logger.info(LogMsg.to("msg", "该水表不存在或不唯一,不进行数据上报", "combineModel", JSONUtils.toJSONString(combineModel)));
            // TODO 是否允许升级
            return;
        }
        String enterpriseid = list.get(0).getEnterpriseid();

        // 内部登录
        DockingForm dockingForm = new DockingForm();
        dockingForm.setType(DockingConstants.KBL_NB_USER_INFO);
        dockingForm.setEnterpriseid(enterpriseid);
        DockingVo dockingVo = iMeterDockingFactory.get(dockingForm);

        // 数据处理
        // 设备
        DeviceForm deviceForm = new DeviceForm();
//        deviceForm.setDevno(combineModel.getDevno1() + "A" + combineModel.getDevno2());
        deviceForm.setDevno(combineModel.getDevno1());
        deviceForm.setDeveui(combineModel.getImei());
        // TODO XXX
//        deviceForm.setDevno("20190407200415");
//        deviceForm.setDeveui("860705044123704");

        deviceForm.setWater(new BigDecimal(combineModel.getWater()).doubleValue());
        deviceForm.setTapstatus(1);
        deviceForm.setTaptype(0);
        deviceForm.setImsi(null);
        deviceForm.setIccid(combineModel.getCcid());
        deviceForm.setEnterpriseid(enterpriseid);
        // 上报
        List<DeviceUplinkForm> deviceUplinkForms = new ArrayList<>();
        DeviceUplinkForm deviceUplinkForm = new DeviceUplinkForm();
        deviceUplinkForm.setDevno(deviceForm.getDevno());
        deviceUplinkForm.setUplinkDate(new Date());

//        double v = (new BigDecimal(combineModel.getWaterConsumption()).divide(new BigDecimal(1000))).doubleValue();
        deviceUplinkForm.setWater(new BigDecimal(combineModel.getWaterConsumption()).doubleValue());
        deviceUplinkForm.setTapstatus(1);
        deviceUplinkForm.setIccid(combineModel.getCcid());
        deviceUplinkForm.setUplinkData(JSONUtils.toJSONString(combineModel));
        deviceUplinkForm.setRssi(combineModel.getRssi());
        deviceUplinkForm.setLsnr(combineModel.getSnr());
        deviceUplinkForm.setEnterpriseid(enterpriseid);
        deviceUplinkForms.add(deviceUplinkForm);
        deviceForm.setDeviceUplinkForms(deviceUplinkForms);
        // 处理
        iKblDeviceFactory.updateDevice(BeanUtils.copy(dockingVo, DockingForm.class), deviceForm);
    }

    /**
     * 响应
     */
    private void response(ChannelHandlerContext ctx, CombineModel combineModel) throws FrameworkRuntimeException {
        String slaveHex = combineModel.getSlaveHex();
        // 发送回复
        // TODO 根据版本号读取升级文件
        String load = load();
//        logger.info(LogMsg.to("load", load.toUpperCase()));
        // TODO 缓存升级文件
        int loadLen = load.length();
        // loadLen = loadLen + 256;
        // 总共帧数
        int total = (loadLen % 640 == 0 ? loadLen / 640 : loadLen / 640 + 1);
        String totalHex = NumberUtils.tenToHexStr(String.valueOf(total), 2);
        // 主站发送的当前帧，响应时主站和从站一样
        String masterHex = slaveHex;
        // 从站需要的当前帧
        int slave = NumberUtils.hexToTen(slaveHex);
        // 响应时从站0000需要转为CCCC
        if ("0000".equalsIgnoreCase(slaveHex)) {
            slaveHex = "CCCC";
        }
        // 数据帧内容长度
        String lenHex = "0140";
        // 数据帧内容
        String contentHex = "";
        if ((slave + 1) * 640 <= load.length()) {
            contentHex = load.substring(slave * 640, (slave + 1) * 640);
        } else if ((slave + 1) * 640 <= (load.length() + 640)) {
            contentHex = load.substring(slave * 640);
        }
        while (contentHex.length() < 640) {
            contentHex = contentHex + "0";
        }
        List<String> contentHexs = StringUtils.getStrList(contentHex, 2);
        List<Integer> contents = NumberUtils.hexToTen(contentHexs);
        // 数据帧内容校验
        int crc16 = crc16(contents, contents.size());
        // CRC校验
        String crcHex = NumberUtils.tenToHexStr(String.valueOf(crc16), 2);
        // 响应
        String resp = (MSG_HEAD + totalHex + masterHex + slaveHex + lenHex + contentHex + crcHex).toUpperCase();
//        logger.info(LogMsg.to("resp", resp));
        if (slaveHex.equalsIgnoreCase(totalHex)) {
            logger.info(LogMsg.to("msg", "升级成功完成了" + slaveHex, "combineModel", JSONUtils.toJSONString(combineModel)));
        } else {
            logger.info(LogMsg.to("msg", "升级中了" + slaveHex, "combineModel", JSONUtils.toJSONString(combineModel)));
        }
        sendMsg(ctx, resp);
    }

    /**
     * 响应
     */
    @Deprecated
    private void response2(ChannelHandlerContext ctx, CombineModel combineModel) throws FrameworkRuntimeException {
        String slaveHex = combineModel.getSlaveHex();
        // 发送回复
        // 总共帧数
        int total = 0;
        String totalHex = NumberUtils.tenToHexStr(String.valueOf(total), 2);
        // 主站发送的当前帧，响应时主站和从站一样
        String masterHex = slaveHex;
        // 从站需要的当前帧，响应时从站0000需要转为CCCC
        int slave = NumberUtils.hexToTen(slaveHex);
        if ("0000".equalsIgnoreCase(slaveHex)) {
            slaveHex = "CCCC";
        }
        // 数据帧内容长度
        String lenHex = "0140";
        // 数据帧内容
        String contentHex = "";
        while (contentHex.length() < 640) {
            contentHex = contentHex + "0";
        }
        List<String> contentHexs = StringUtils.getStrList(contentHex, 2);
        List<Integer> contents = NumberUtils.hexToTen(contentHexs);
        // 数据帧内容校验
        int crc16 = crc16(contents, contents.size());
        // CRC校验
        String crcHex = NumberUtils.tenToHexStr(String.valueOf(crc16), 2);
        // 响应
        String resp = (MSG_HEAD + totalHex + masterHex + slaveHex + lenHex + contentHex + crcHex).toUpperCase();
        logger.info(LogMsg.to("resp", resp));
        logger.info(LogMsg.to("msg", "不需要升级", "combineModel", combineModel));
        sendMsg(ctx, resp);
    }

    /**
     * crc校验
     */
    private final int crc16(List<Integer> ptr, int len) {
        if (ptr.isEmpty()) {
            return 0;
        }
        //预置16位crc寄存器，初值全部为1
        int wcrc = 0XFFFF;
        //定义中间变量
        int temp = 0;
        //定义计数
        int i = 0, j = 0;
        //循环计算每个数据
        for (i = 0; i < len; i++) {
            //将八位数据与crc寄存器亦或
            temp = ptr.get(i) & 0X00FF;
            // ptr++;  //指针地址增加，指向下个数据
            //将数据存入crc寄存器
            wcrc ^= temp;
            //循环计算数据的
            for (j = 0; j < 8; j++) {
                //判断右移出的是不是1，如果是1则与多项式进行异或。
                if ((wcrc & 0X0001) == 1) {
                    //先将数据右移一位
                    wcrc >>= 1;
                    //与上面的多项式进行异或
                    wcrc ^= 0XA001;
                } else {
                    //如果不是1，则直接移出
                    //直接移出
                    wcrc >>= 1;
                }
            }
        }
        return wcrc;
    }

    /**
     * 加载bin文件
     */
    private String load() throws FrameworkRuntimeException {
        String bin = svc.get(FILE_KEY);
//        logger.info(LogMsg.to(FILE_KEY, bin));
        if (StringUtils.isNoneEmpty(bin)) {
            return bin;
        }
        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        try {
            inputStream = new FileInputStream(filePath);
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            byte[] data = new byte[4096];
            int count = inputStream.read(data);
            while (count != -1) {
                dos.write(data, 0, count);
                count = inputStream.read(data);
            }
            byte[] bytes = baos.toByteArray();
            bin = NumberUtils.bytesToHex(bytes);
            bin = loadVerify(bin);
            // 缓存file-bin 缓存6小时
            svc.set(FILE_KEY, bin, 21600);
            return bin;
        } catch (Exception e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.UPFILE_FORMAT_ERROR);
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * bin文件增加校验码替换
     */
    private String loadVerify(String bin) {
        String crcHex = bin.substring(128 * 2);
        List<String> strList = StringUtils.getStrList(crcHex, 2);
        int total = 0;
        for (String str : strList) {
            int crc = NumberUtils.hexToTen(str);
            total += crc;
        }
        System.out.println(total);
        int a12 = (char) ((total >> 24) & 0xff);
        int a13 = (char) ((total >> 16) & 0xff);
        int a14 = (char) ((total >> 8) & 0xff);
        int a15 = (char) (total & 0xff);
        System.out.println(a12);
        System.out.println(a13);
        System.out.println(a14);
        System.out.println(a15);
        // 45198 22599
        int size = (bin.length() + 128 * 2) / 2;
        System.out.println(size);
        int a16 = (char) ((size >> 24) & 0xff);
        int a17 = (char) ((size >> 16) & 0xff);
        int a18 = (char) ((size >> 8) & 0xff);
        int a19 = (char) (size & 0xff);
        System.out.println(a16);
        System.out.println(a17);
        System.out.println(a18);
        System.out.println(a19);
        // 替换bin内容
        StringBuilder sb = new StringBuilder(bin);
        sb.replace(24, 26, NumberUtils.tenToHexStr(String.valueOf(a12), 1));
        sb.replace(26, 28, NumberUtils.tenToHexStr(String.valueOf(a13), 1));
        sb.replace(28, 30, NumberUtils.tenToHexStr(String.valueOf(a14), 1));
        sb.replace(30, 32, NumberUtils.tenToHexStr(String.valueOf(a15), 1));
        sb.replace(32, 34, NumberUtils.tenToHexStr(String.valueOf(a16), 1));
        sb.replace(34, 36, NumberUtils.tenToHexStr(String.valueOf(a17), 1));
        sb.replace(36, 38, NumberUtils.tenToHexStr(String.valueOf(a18), 1));
        sb.replace(38, 40, NumberUtils.tenToHexStr(String.valueOf(a19), 1));
        System.out.println(bin);
        System.out.println(sb.toString());
        return sb.toString();
    }

    /**
     * 接收对象
     */
    @Getter
    @Setter
    class CombineModel {

        /**
         * 头部校验是否合法
         */
        private boolean ifHead;
        /**
         * 长度校验是否合法
         */
        private boolean ifLength;
        /**
         * crc校验是否合法
         */
        private boolean ifCrc;
        /**
         * 内容头部是否合法
         */
        private boolean ifContentHead;
        /**
         * 内容是否合法
         */
        private boolean ifContent;

        /**
         * 总共帧数
         */
        private String totalHex;
        /**
         * 主站发送的当前帧
         */
        private String masterHex;
        /**
         * 从站需要的当前帧
         */
        private String slaveHex;
        /**
         * 数据帧内容长度
         */
        private String lenHex;
        /**
         * 数据帧内容
         */
        private String contentHex;

        /**
         * 软件版本号
         */

        private String versionHex;
        private String version;
        /**
         * 序列号(装置号区域1)
         */
        private String devno1Hex;
        private String devno1;
        /**
         * 装置号(装置号区域2)
         */
        private String devno2Hex;
        private String devno2;
        /**
         * 正累计
         */
        private String waterHex;
        private String water;
        /**
         * 负累计
         */
        private String nWaterHex;
        private String nWater;
        /**
         * 霍尔1计数
         */
        private String hall1Hex;
        private String hall1;
        /**
         * 霍尔2计数
         */
        private String hall2Hex;
        private String hall2;
        /**
         * SIM卡CCID
         */
        private String ccidHex;
        private String ccid;
        /**
         * IMEI号
         */
        private String imeiHex;
        private String imei;

        // 1000009新增
        /**
         * earfcn
         */
        private String earfcn;
        /**
         * EARFCN_OFFSET
         */
        private String earfcnOffset;
        /**
         * PCI
         */
        private String pci;
        /**
         * CELID
         */
        private String celid;
        /**
         * RSRP
         */
        private String rsrp;
        /**
         * RSRQ
         */
        private String rsrq;
        /**
         * RSSI
         */
        private String rssi;
        /**
         * SNR
         */
        private String snr;

        /**
         * 水表计量单位
         */
        private String unit;
        /**
         * 水表行数
         */
        private String waterConsumption;
        /**
         * 复位次数
         */
        private String resetTimes;
        /**
         * NB模块开启次数
         */
        private String openNBTimes;
        /**
         * 后台连接成功测试
         * Successful background connection test
         */
        private String sbct;
        /**
         * 水表底数
         */
        private String waterMeterBase;
        /**
         * NB上送周期
         */
        private String uploadNBCycle;
        /**
         * 电池电压
         */
        private String batteryVoltage;
        /**
         * 磁暴标志
         * 0无磁暴
         * 1磁暴
         */
        private String magneticStorm;

    }
}
