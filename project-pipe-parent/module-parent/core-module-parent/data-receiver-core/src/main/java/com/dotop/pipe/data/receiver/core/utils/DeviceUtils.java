package com.dotop.pipe.data.receiver.core.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dotop.pipe.data.receiver.core.constants.FieldConstants;
import com.dotop.pipe.data.receiver.core.model.DeviceRD;
import com.dotop.pipe.data.receiver.core.model.InfoRD;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.pipe.data.receiver.core.model.PropertyRD;
import com.dotop.smartwater.dependence.core.log.LogMsg;

public class DeviceUtils {

    private final static Logger logger = LogManager.getLogger(DeviceUtils.class);

    // 流量计
    public final static DeviceRD parse(Date curr, String content, JSONObject rddata, DeviceVo device) {
        DeviceRD deviceRD = parseObj(content);
        InfoRD info = deviceRD.getInfo();
        List<PropertyRD> properties = deviceRD.getProperties();
        Map<String, PropertyRD> propertyMap = propertyMap(properties);
        Date fsd = null;
        // TODO 校验非空、类别、类型
        ProductVo product = device.getProduct();
        if (product == null) {
            return null;
        }
        String productCategory = product.getCategory().getVal();
        String productType = product.getType().getVal();
        if (StringUtils.isBlank(productCategory) || StringUtils.isBlank(productType)) {
            return null;
        }
        switch (productType) {
            case FieldConstants.SENSOR_FLW:
                // 获取流量编码
                PropertyRD flwCode = propertyMap.get(FieldConstants.FLW_CODE);
                draw(flwCode, rddata);
                // 获取流速
                PropertyRD flwRate = propertyMap.get(FieldConstants.FLW_RATE);
                draw(flwRate, rddata);
                // 行度计算 时间间隔 约定5分钟一次 流速 * 时间 即 flwRate * 5 / 60 = 这段时间的行度
                // 获取行度解析模型
                PropertyRD flwValve = propertyMap.get(FieldConstants.FLW_MEASURE); // 获取流量计行度解析模型
                draw(flwValve, flwRate, rddata);
                // 获取总行度
                PropertyRD flwTotalValue = propertyMap.get(FieldConstants.FLW_TOTAL_VALUE);
                draw(flwTotalValue, rddata);
                // 获取发送时间
                PropertyRD flwSendDate = propertyMap.get(FieldConstants.FLW_SEND_DATE);
                flwSendDate = draw(flwSendDate, rddata);
                fsd = drawValDate(flwSendDate.getVal(), flwSendDate.getType());
                // 基本参数
                info.setCode(flwCode.getVal());
                info.setSerReceDate(curr);
                info.setDevSendDate(fsd);
                break;
            case FieldConstants.SENSOR_PRESSURE:
                // 获取压力计编码
                PropertyRD preCode = propertyMap.get(FieldConstants.PRESSURE_CODE);
                draw(preCode, rddata);
                // 获取流速
                PropertyRD preRate = propertyMap.get(FieldConstants.PRESSURE_RATE);
                draw(preRate, rddata);
                // 获取发送时间
                PropertyRD preSendDate = propertyMap.get(FieldConstants.PRESSURE_SEND_DATE);
                preSendDate = draw(preSendDate, rddata);
                fsd = drawValDate(preSendDate.getVal(), preSendDate.getType());
                // 基本参数
                info.setCode(preCode.getVal());
                info.setSerReceDate(curr);
                info.setDevSendDate(fsd);
                break;
            case FieldConstants.SENSOR_QUALITY:
                // 获取水质计编码
                PropertyRD quaCode = propertyMap.get(FieldConstants.QUALITY_CODE);
                draw(quaCode, rddata);
                // PH值
                PropertyRD quaPh = propertyMap.get(FieldConstants.QUALITY_PH);
                draw(quaPh, rddata);
                // 含氧
                PropertyRD quaOxygen = propertyMap.get(FieldConstants.QUALITY_OXYGEN);
                draw(quaOxygen, rddata);
                // 含氯
                PropertyRD quaChlorine = propertyMap.get(FieldConstants.QUALITY_CHLORINE);
                draw(quaChlorine, rddata);
                // 浑浊
                PropertyRD quaTurbid = propertyMap.get(FieldConstants.QUALITY_TURBID);
                draw(quaTurbid, rddata);

                // 一号温度
                PropertyRD quaTemOne = propertyMap.get(FieldConstants.QUALITY_TEM_ONE);
                draw(quaTemOne, rddata);

                // 二号温度
                PropertyRD quaTemTwo = propertyMap.get(FieldConstants.QUALITY_TEM_TWO);
                draw(quaTemTwo, rddata);

                // 三号温度
                PropertyRD quaTemThree = propertyMap.get(FieldConstants.QUALITY_TEM_THREE);
                draw(quaTemThree, rddata);
                // 四号温度
                PropertyRD quaTemFour = propertyMap.get(FieldConstants.QUALITY_TEM_FOUR);
                draw(quaTemFour, rddata);
                // 获取发送时间
                PropertyRD quaSendDate = propertyMap.get(FieldConstants.QUALITY_SEND_DATE);
                quaSendDate = draw(quaSendDate, rddata);

                fsd = drawValDate(quaSendDate.getVal(), quaSendDate.getType());
                // 基本参数
                info.setCode(quaCode.getVal());
                info.setSerReceDate(curr);
                info.setDevSendDate(fsd);
                break;
            default:
                break;
        }
        return deviceRD;
    }

    private final static DeviceRD parseObj(String content) {
        logger.info(LogMsg.to("content", content));
        DeviceRD deviceRD = JSON.parseObject(content, DeviceRD.class);
        logger.info(LogMsg.to("info", deviceRD.getInfo()), "properties", deviceRD.getProperties());
        return deviceRD;
    }

    private final static Map<String, PropertyRD> propertyMap(List<PropertyRD> properties) {
        Map<String, PropertyRD> map = new HashMap<String, PropertyRD>();
        for (PropertyRD property : properties) {
            String field = property.getField();
            map.put(field, property);
        }
        return map;
    }

    private final static PropertyRD draw(PropertyRD rd, JSONObject rddata) {
        String tag = rd.getTag();
        String rddataVal = rddata.getString(tag); // 根据tag 在json 数据中找到 属性值
        if (StringUtils.isBlank(rddataVal)) {
            return rd;
        }
        String type = rd.getType(); // 字段类型
        verify(rddataVal, type);
        rd.setVal(rddataVal);
        return rd;
    }

    private final static PropertyRD draw(PropertyRD flwValve, PropertyRD flwRate, JSONObject rddata) {
        String tag = flwRate.getTag(); // 获取流速的值
        String rddataVal = rddata.getString(tag); // 流速值

        if (StringUtils.isBlank(rddataVal)) {
            return flwValve;
        }
        // 计算行度数据
        BigDecimal bigflwRate = new BigDecimal(rddataVal);
        BigDecimal bignum2 = new BigDecimal("5");
        BigDecimal bignum3 = new BigDecimal("60");
        String flwValveStr = bigflwRate.multiply(bignum2).divide(bignum3, 6).toString();
        String type = flwValve.getType(); // 字段类型
        verify(flwValveStr, type);
        flwValve.setVal(flwValveStr);
        return flwValve;
    }

    /**
     * 计算行度值
     *
     * @param rddataVal
     * @return
     */
    public static String calcuMeasure(String rddataVal) {
        if (rddataVal == null) {
            return "";
        }
        // 计算行度数据
        BigDecimal bigflwRate = new BigDecimal(rddataVal);
        BigDecimal bignum2 = new BigDecimal("5");
        BigDecimal bignum3 = new BigDecimal("60");
        String flwValveStr = bigflwRate.multiply(bignum2).divide(bignum3, 6).toString();
        return flwValveStr;
    }

    private final static boolean verify(String val, String type) {
        // 类型校验
        String cont = null;
        if (type.startsWith("number")) {
            cont = type.substring(6 + 1, type.length() - 1);
            return true;
        } else if (type.startsWith("string")) {
            cont = type.substring(6 + 1, type.length() - 1);
            return true;
        } else if (type.startsWith("date")) {
            cont = type.substring(4 + 1, type.length() - 1);
            return true;
        }
        System.out.println(cont);
        return false;
    }

    private final static Date drawValDate(String val, String type) {
        if (type.startsWith("date")) {
            String cont = type.substring(4 + 1, type.length() - 1);
            DateTimeFormatter formatter = DateTimeFormat.forPattern(cont);
            return DateTime.parse(val, formatter).toDate();
        }
        return null;
    }

    public static Map<String, Object> getPropertyMap() {
        Map<String, Object> devicePropertyLogMap = new HashMap<>();
        devicePropertyLogMap.put(FieldConstants.KBL_QUALITY_CHLORINE, null);
        devicePropertyLogMap.put(FieldConstants.KBL_QUALITY_OXYGEN, null);
        devicePropertyLogMap.put(FieldConstants.KBL_QUALITY_PH, null);
        devicePropertyLogMap.put(FieldConstants.KBL_QUALITY_TURBID, null);
        devicePropertyLogMap.put(FieldConstants.KBL_QUALITY_TEM_ONE, null);
        devicePropertyLogMap.put(FieldConstants.KBL_QUALITY_TEM_TWO, null);
        devicePropertyLogMap.put(FieldConstants.KBL_QUALITY_TEM_THREE, null);
        devicePropertyLogMap.put(FieldConstants.KBL_QUALITY_TEM_FOUR, null);
        devicePropertyLogMap.put(FieldConstants.KBL_FLW_MEASURE, null);
        devicePropertyLogMap.put(FieldConstants.KBL_FLW_RATE, null);
        devicePropertyLogMap.put(FieldConstants.KBL_FLW_TOTAL_VALUE, null);
        devicePropertyLogMap.put(FieldConstants.KBL_PRESSURE_VALUE, null);
        devicePropertyLogMap.put(FieldConstants.KBL_DEVICE_CODE, null);
        devicePropertyLogMap.put(FieldConstants.KBL_DEVICE_SEND_DATE, null);
        devicePropertyLogMap.put(FieldConstants.KBL_HYDRANT_BUMP, null);
        devicePropertyLogMap.put(FieldConstants.KBL_HYDRANT_HIGH_LOW_ALARM, null);
        devicePropertyLogMap.put(FieldConstants.KBL_HYDRANT_SLOPE, null);
        return devicePropertyLogMap;
    }
}
