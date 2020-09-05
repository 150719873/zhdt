package com.dotop.pipe.core.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dotop.pipe.core.enums.FieldTypeEnum;
import com.dotop.pipe.core.vo.alarm.AlarmSettingVo;
import com.dotop.pipe.core.vo.alarm.AlarmWMSettingVo;
import com.dotop.pipe.core.vo.device.DevicePropertyFieldVo;

/**
 * 报表处理工具类
 *
 *
 */
public class ReportUtils {

    // 统计报表默认的展示字段
    public static Map<String, Object> propertyObject = new HashMap<>();

    // 说明: field:flwRate tag:flw_rate
    static {

        for (FieldTypeEnum e : FieldTypeEnum.values()) {
            propertyObject.put(e.getCode(), new DevicePropertyFieldVo(e.getName(), e.getCode(), e.getLowcode(), e.getUnit()));
        }
        propertyObject.put("sendDate", new DevicePropertyFieldVo("上传时间", "sendDate", "send_date", ""));
//        propertyObject.put("flwRate", new DevicePropertyFieldVo("流量", "flwRate", "flw_rate", "m3/h"));
//        propertyObject.put("flwTotalValue", new DevicePropertyFieldVo("总行度", "flwTotalValue", "flw_total_value", "m3"));
//        propertyObject.put("flwMeasure", new DevicePropertyFieldVo("行度", "flwMeasure", "flw_measure", "m3"));
//        propertyObject.put("pressureValue", new DevicePropertyFieldVo("压力值", "pressureValue", "pressure_value", "MPa"));
//        propertyObject.put("qualityTemOne",
//                new DevicePropertyFieldVo("一号温度", "qualityTemOne", "quality_tem_one", "°C"));
//        propertyObject.put("qualityTemTwo",
//                new DevicePropertyFieldVo("二号温度", "qualityTemTwo", "quality_tem_two", "°C"));
//        propertyObject.put("qualityTemThree",
//                new DevicePropertyFieldVo("三号温度", "qualityTemThree", "quality_tem_three", "°C"));
//        propertyObject.put("qualityTemFour",
//                new DevicePropertyFieldVo("四号温度", "qualityTemFour", "quality_tem_four", "°C"));
//        propertyObject.put("qualityChlorine",
//                new DevicePropertyFieldVo("含氯值", "qualityChlorine", "quality_chlorine", "mg/L"));
//        propertyObject.put("qualityOxygen",
//                new DevicePropertyFieldVo("含氧值", "qualityOxygen", "quality_oxygen", "mg/L"));
//        propertyObject.put("qualityPh", new DevicePropertyFieldVo("PH值", "qualityPh", "quality_ph", ""));
//        propertyObject.put("qualityTurbid", new DevicePropertyFieldVo("浑浊度", "qualityTurbid", "quality_turbid", "NTU"));

    }

    /**
     * 水质预警 结合配置参数 处理数据信息 统计 实时数据查询出有数据的字段 没有数据的字段默认没有 上报的属性
     */
    @SuppressWarnings("unchecked")
    public static List<?> alarmWMToList(Object object, Map<String, AlarmSettingVo> map) {
        List list = new ArrayList<>();
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i].getName();
                fields[i].setAccessible(true);
                if (fields[i].get(object) == null) {
                    continue;
                }
                String val = fields[i].get(object).toString();
                if (!"".equals(val) && propertyObject.containsKey(fieldName)) {
                    DevicePropertyFieldVo devicePropertyFieldVo = (DevicePropertyFieldVo) propertyObject.get(fieldName);
                    AlarmWMSettingVo alarmWMSettingVo = new AlarmWMSettingVo(devicePropertyFieldVo.getName(), fieldName,
                            val, devicePropertyFieldVo.getUnit(),
                            map.containsKey(fieldName) ? map.get(fieldName) : null);
                    if (map.containsKey(fieldName)) {
                        boolean flag = AlarmCalulationUtils.alarmCalulation(map.get(fieldName), val);
                        alarmWMSettingVo.setStatus(flag);
                    }
                    // 计算是否异常
                    list.add(alarmWMSettingVo);
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return list;

    }

}
