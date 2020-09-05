package com.dotop.pipe.core.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dotop.pipe.core.enums.FieldTypeEnum;

import com.dotop.pipe.core.vo.device.DevicePropertyVo;

/**
 * 将实体类的属性字段 转为集合封装
 *
 *
 * @date 2019年6月3日
 */
public class ColumnToListUtils {

    public static Map<String, String> filedNameMap = new HashMap<>();

    static {
        filedNameMap = FieldTypeEnum.getFiledMap();
//        filedNameMap.put("flwRate", "m³/h");
//        filedNameMap.put("flwTotalValue", "m3");
//        filedNameMap.put("flwMeasure", "m3");
//        filedNameMap.put("pressureValue", "MPa");
//        filedNameMap.put("qualityTemOne", "°C");
//        filedNameMap.put("qualityTemTwo", "°C");
//        filedNameMap.put("qualityTemThree", "°C");
//        filedNameMap.put("qualityTemFour", "°C");
//        filedNameMap.put("qualityChlorine", "mg/L");
//        filedNameMap.put("qualityOxygen", "mg/L");
//        filedNameMap.put("qualityPh", "");
//        filedNameMap.put("qualityTurbid", "NTU");
//        filedNameMap.put("slope", "°");
//        filedNameMap.put("highLowAlarm", "");
//        filedNameMap.put("bump", " ");
    }

    public final static List<DevicePropertyVo> toList(DevicePropertyVo object) {
        Field[] fields = object.getClass().getDeclaredFields();
        List<DevicePropertyVo> list = new ArrayList<>();
        try {
            for (int i = 0; i < fields.length; i++) {
                // 设置是否允许访问，不是修改原来的访问权限修饰词。
                DevicePropertyVo vo = new DevicePropertyVo();
                vo.setDeviceId(object.getDeviceId());
                vo.setDeviceCode(object.getDeviceCode());
                vo.setDevSendDate(object.getDevSendDate());
                fields[i].setAccessible(true);
                String fieldName = fields[i].getName();
                if (null != fields[i].get(object) && filedNameMap.containsKey(fieldName)) {
                    // 给这个字段赋值
                    // Field name = vo.getClass().getDeclaredField(fieldName + "");
                    // name.setAccessible(true);
                    // name.set(vo, fields[i].get(object));
                    // name.setAccessible(false);
                    vo.setField(fieldName);
                    vo.setVal(fields[i].get(object).toString());
                    vo.setUnit(filedNameMap.get(fieldName));
                    list.add(vo);
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return list;
    }

    public final static List<DevicePropertyVo> toList(List<DevicePropertyVo> Volist) {
        List<DevicePropertyVo> list = new ArrayList<>();
        try {
            for (DevicePropertyVo object : Volist) {
                Field[] fields = object.getClass().getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    // 设置是否允许访问，不是修改原来的访问权限修饰词。
                    DevicePropertyVo vo = new DevicePropertyVo();
                    vo.setDeviceId(object.getDeviceId());
                    vo.setDeviceCode(object.getDeviceCode());
                    vo.setDevSendDate(object.getDevSendDate());
                    fields[i].setAccessible(true);
                    String fieldName = fields[i].getName();
                    if (null != fields[i].get(object) && filedNameMap.containsKey(fieldName)) {
                        vo.setField(fieldName);
                        vo.setVal(fields[i].get(object).toString());
                        vo.setUnit(filedNameMap.get(fieldName));
                        list.add(vo);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        DevicePropertyVo devicePropertyVo = new DevicePropertyVo();
        devicePropertyVo.setDeviceCode("1222");
        devicePropertyVo.setFlwMeasure("121");
        ColumnToListUtils ll = new ColumnToListUtils();
        ll.toList(devicePropertyVo);
        System.out.println(ll.toList(devicePropertyVo));
    }

}
