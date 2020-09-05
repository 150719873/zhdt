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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class CombineMsgHandler0_1 extends MsgHandler {

    private static final Logger logger = LogManager.getLogger(CombineMsgHandler0_1.class);

    @Value("${combine.file.path}")
    private String filePath;

    private static final String MSG_HEAD = "A55AE9B0";

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
            logger.info("msg:{}", msg);
            logger.info("开始校验数据----------------------");
            JSONObjects jsonObjects = JSONUtils.parseObject(msg);
            String dataHex = jsonObjects.getString("data");
            dataHex = dataHex.replaceAll("\\s*", "");
            logger.info(LogMsg.to("dataHex", dataHex));

            // 校验
            CombineModel combineModel = crc(ctx, dataHex);
            if (combineModel == null) {
                return;
            }
            // 内容
            content(combineModel);
            // INSERT INTO `dictionary_child` (`child_id`, `dictionary_id`, `child_name`, `child_value`, `remark`, `create_by`, `create_date`, `last_by`, `last_date`, `is_del`) VALUES ('28,300001,11', '28,300001', '康宝莱', '11', '', '1', '2019-10-12 16:59:38', '1', '2019-10-12 16:59:38', '0');
            // 水务设备处理
            water(combineModel);
            // 如果此版本则升级
            // if ("1000004".equalsIgnoreCase(combineModel.getVersion())) {
            // 响应
            response(ctx, combineModel);
            // } else {
            // response2(ctx, combineModel);
            // }
            logger.info("更新抄表数据成功------------");
        } catch (Exception e) {
            logger.info(LogMsg.to("ctx", ctx, "Exception", e));
        }
    }

    /**
     * 校验
     */
    private CombineModel crc(ChannelHandlerContext ctx, String dataHex) throws FrameworkRuntimeException {
        // 同步头校验
        if (StringUtils.isBlank(dataHex) || dataHex.length() < 8) {
            logger.info(LogMsg.to("msg", "校验头部失败"));
            ctx.close();
            return null;
        }
        String headHex = dataHex.substring(0, 8);
        if (!MSG_HEAD.equalsIgnoreCase(headHex)) {
            logger.info(LogMsg.to("msg", "校验头部失败"));
            ctx.close();
            return null;
        }
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
        String contentHex = dataHex.substring(24, 24 + len * 2);
        List<String> contentHexs = StringUtils.getStrList(contentHex, 2);
        List<Integer> contents = NumberUtils.hexToTen(contentHexs);
        // 数据帧内容校验
        int crc16 = crc16(contents, 28);
        // CRC校验
        String crcHex = dataHex.substring(24 + len * 2);
        int crc = NumberUtils.hexToTen(crcHex);
        logger.info(LogMsg.to("totalHex", totalHex, "masterHex", masterHex, "slaveHex", slaveHex, "lenHex", lenHex, "len", len, "contentHex", contentHex, "crcHex", crcHex, "crc", crc, "crc16", crc16));
        if (crc != crc16) {
            logger.info(LogMsg.to("msg", "校验CRC失败"));
            ctx.close();
            return null;
        }
        // 返回解析模型
        CombineModel combineModel = new CombineModel();
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
        String contentHex = combineModel.getContentHex();
        // 数据帧内容解析
        // 软件版本号
        String versionHex = contentHex.substring(0, 8);
        String version = NumberUtils.hexToTenStr(versionHex);
        // 装置号区域1
        String devno1Hex = contentHex.substring(8, 16);
        String devno1 = NumberUtils.hexToTenStr(devno1Hex);
        // 装置号区域2
        String devno2Hex = contentHex.substring(16, 24);
        String devno2 = NumberUtils.hexToTenStr(devno2Hex);
        // 正累计
        String waterHex = contentHex.substring(24, 32);
        String water = NumberUtils.hexToTenStr(waterHex);
        water = new BigDecimal(water).divide(new BigDecimal(100)).toString();

        // 负累计
        String nwaterHex = contentHex.substring(32, 40);
        String nwater = NumberUtils.hexToTenStr(nwaterHex);
        // 霍尔1计数
        String hall1Hex = contentHex.substring(40, 48);
        String hall1 = NumberUtils.hexToTenStr(hall1Hex);
        // 霍尔2计数
        String hall2Hex = contentHex.substring(48, 56);
        String hall2 = NumberUtils.hexToTenStr(hall2Hex);
        // SIM卡CCID
        String ccidHex = contentHex.substring(56, 76);
        String ccid = ccidHex;
        // IMEI号
        String imeiHex = contentHex.substring(76, 92);
        String imei = imeiHex.substring(0, 15);
        logger.info(LogMsg.to("versionHex", versionHex, "devno1Hex", devno1Hex, "devno2Hex", devno2Hex, "waterHex", waterHex, "nwaterHex", nwaterHex, "hall1Hex", hall1Hex, "hall2Hex", hall2Hex, "ccidHex", ccidHex, "imeiHex", imeiHex));
        logger.info(LogMsg.to("version", version, "devno1", devno1, "devno2", devno2, "water", water, "nwater", nwater, "hall1", hall1, "hall2", hall2, "ccid", ccid, "imei", imei));
        // 返回解析模型
        combineModel.setVersionHex(versionHex);
        combineModel.setVersion(version);
        combineModel.setDevno1Hex(devno1Hex);
        combineModel.setDevno1(devno1);
        combineModel.setDevno2Hex(devno2Hex);
        combineModel.setDevno2(devno2);
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
        deviceBo.setDevno(combineModel.getDevno1() + "A" + combineModel.getDevno2());
        deviceBo.setDeveui(combineModel.getImei());
        List<DeviceVo> list = iWaterDeviceService.list(deviceBo);
        if (list == null || list.size() != 1) {
            logger.info(LogMsg.to("msg", "该水表不存在或不唯一,不进行数据上报", "combineModel", combineModel));
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
        deviceForm.setDevno(combineModel.getDevno1() + "A" + combineModel.getDevno2());
        deviceForm.setDeveui(combineModel.getImei());
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
        deviceUplinkForm.setWater(new BigDecimal(combineModel.getWater()).doubleValue());
        deviceUplinkForm.setTapstatus(1);
        deviceUplinkForm.setIccid(combineModel.getCcid());
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
//        if ("0000".equalsIgnoreCase(slaveHex)) {
//            slaveHex = "CCCC";
//        }
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
        int crc16 = crc16(contents, 320);
        // CRC校验
        String crcHex = NumberUtils.tenToHexStr(String.valueOf(crc16), 2);
        // 响应
        String resp = (MSG_HEAD + totalHex + masterHex + slaveHex + lenHex + contentHex + crcHex).toUpperCase();
        logger.info(LogMsg.to("resp", resp));
        if (slaveHex.equalsIgnoreCase(totalHex)) {
            logger.info(LogMsg.to("msg", "升级成功", "combineModel", combineModel));
        } else {
            logger.info(LogMsg.to("msg", "升级中" + slaveHex, "combineModel", combineModel));
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
        int crc16 = crc16(contents, 320);
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
        try {
            InputStream inputStream = new FileInputStream(filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            byte[] data = new byte[4096];
            int count = inputStream.read(data);
            while (count != -1) {
                dos.write(data, 0, count);
                count = inputStream.read(data);
            }
            byte[] bytes = baos.toByteArray();
            return NumberUtils.bytesToHex(bytes);
        } catch (Exception e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.UPFILE_FORMAT_ERROR);
        }
    }

    /**
     * 接收对象
     */
    @Getter
    @Setter
    class CombineModel {
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
         * 装置号区域1
         */
        private String devno1Hex;
        private String devno1;
        /**
         * 装置号区域2
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
    }
}
