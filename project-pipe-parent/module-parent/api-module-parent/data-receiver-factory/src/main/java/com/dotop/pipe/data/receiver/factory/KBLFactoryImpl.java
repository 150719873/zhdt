package com.dotop.pipe.data.receiver.factory;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.dotop.pipe.data.receiver.core.constants.FieldConstants;
import com.dotop.pipe.data.receiver.core.utils.DeviceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dotop.pipe.api.service.alarm.IAlarmService;
import com.dotop.pipe.api.service.device.IDeviceService;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.data.receiver.api.factory.IDeviceDataFactory;
import com.dotop.pipe.data.receiver.api.factory.IKBLReceiveFactory;

@Component
public class KBLFactoryImpl implements IKBLReceiveFactory {

    private static final Logger LOGGER = LogManager.getLogger(KBLFactoryImpl.class);

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IDeviceDataFactory iDeviceDataFactory;

    // @Autowired
    // private IAlarmFactory iAlarmFactory;

    @Autowired
    private IAlarmService iAlarmService;

    /**
     * 接收数据处理 处理系统允许的格式
     */
    @Override
    public void handle(String deviceCode, JSONObject originalData) {

        Map<String, Object> devicePropertyLogMap = DeviceUtils.getPropertyMap();

        // 先查询缓存 是否存在 存在
        boolean cacheFlag = iDeviceDataFactory.getCacheByDeviceCode(deviceCode);
        boolean flag = true;
        DeviceBo deviceBo = new DeviceBo();
        deviceBo.setCode(deviceCode);
        // 不存在查询数据库
        if (!cacheFlag) {
            // deviceCode getEnterpriseId 有传就不需要查询验证 主要是为了解决 DMA 抄表问题
            if (StringUtils.isBlank(originalData.getString("enterprise_id"))) {
                flag = iDeviceService.isExist(deviceBo);
            }
        }
        if (flag) {
            // 添加到缓存
            iDeviceDataFactory.setCacheByDeviceCode(deviceCode);
            String deviceId = null;
            String enterpriseId = null;
            String productType = null;
            if (StringUtils.isBlank(originalData.getString("enterprise_id"))) {
                // 如果存在查询计量计信息
                DeviceVo device = iDeviceService.get(deviceBo);
                if (device == null) {
                    return;
                }
                deviceId = device.getDeviceId();
                enterpriseId = device.getEnterpriseId();
                productType = device.getProductType();
            } else {
                deviceId = originalData.getString("device_id");
                enterpriseId = originalData.getString("enterprise_id");
                productType = "area";
            }
            JSONObject data = new JSONObject();
            /*
             * 1 解析原始数据 2 类型处理 3 封装成系统允许数据
             */
            Iterator<Entry<String, Object>> itor = originalData.entrySet().iterator();
            while (itor.hasNext()) {
                Entry<String, Object> e = itor.next();
                handleJson(e.getKey(), e.getValue(), devicePropertyLogMap);
            }
            handleAlarm(deviceId, enterpriseId, devicePropertyLogMap, productType);
            // 添加到property表
            iDeviceDataFactory.addProperty(deviceId, enterpriseId, devicePropertyLogMap);
            // 添加到propertyLog表
            // System.out.println(devicePropertyLogMap.toString());
            iDeviceDataFactory.addPropertyLog(originalData, deviceId, enterpriseId, devicePropertyLogMap);
        }
    }

    // 处理数据类型转换 或者报警内容
    public void handleJson(String key, Object val, Map<String, Object> devicePropertyLogMap) {
        switch (key) {
            case "pressure_code":
                devicePropertyLogMap.replace(FieldConstants.KBL_DEVICE_CODE, val.toString());
                break;
            case "flw_code":
                devicePropertyLogMap.replace(FieldConstants.KBL_DEVICE_CODE, val.toString());
                break;
            case "quality_code":
                devicePropertyLogMap.replace(FieldConstants.KBL_DEVICE_CODE, val.toString());
                break;
            case FieldConstants.KBL_FLW_RATE:
                // 流速值 需要根据流速 计算出行度
                devicePropertyLogMap.replace(FieldConstants.KBL_FLW_RATE, val);
                if (val != null) {
                    // 行度值
                    devicePropertyLogMap.replace(FieldConstants.KBL_FLW_MEASURE, DeviceUtils.calcuMeasure(val.toString()));
                }
                break;
            default:
                if (devicePropertyLogMap.containsKey(key)) {
                    devicePropertyLogMap.put(key, val);
                }
                break;
        }
    }

    public void handleAlarm(String deviceId, String enterpriseId, Map<String, Object> devicePropertyLogMap, String productType) {
        devicePropertyLogMap.put("productType", productType);
        iAlarmService.add(deviceId, enterpriseId, devicePropertyLogMap);
        devicePropertyLogMap.remove("productType");
    }
}
