package com.dotop.pipe.core.enums;

import com.dotop.pipe.core.constants.PropertyConstants;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @date 2018/11/2.
 */
public enum FieldTypeEnum {

    /**
     * 流速
     */
    flwRate("流量", PropertyConstants.TYPE_HUMP_FM_RATE, PropertyConstants.TYPE_FM_RATE, "m³/h"),
    /**
     * 总行度
     */
    flwTotalValue("总行度", PropertyConstants.TYPE_HUMP_FM_FLWTOTAL, PropertyConstants.TYPE_FM_FLWTOTAL, "m³"),
    /**
     * 行度
     */
    flwMeasure("行度", PropertyConstants.TYPE_HUMP_FM_FLWMEASURE, PropertyConstants.TYPE_FM_FLWMEASURE, "m³"),
    /**
     * 压力
     */
    pressureValue("压力", PropertyConstants.TYPE_HUMP_PRESSURE_VALUE, PropertyConstants.TYPE_PRESSURE_VALUE, "MPa"),
    /**
     * 一号温度
     */
    qualityTemOne("一号温度", PropertyConstants.TYPE_HUMP_WM_QUALITYTEMONE, PropertyConstants.TYPE_WM_QUALITYTEMONE, "°C"),
    /**
     * 二号温度
     */
    qualityTemTwo("二号温度", PropertyConstants.TYPE_HUMP_WM_QUALITYTEMTWO, PropertyConstants.TYPE_WM_QUALITYTEMTWO, "°C"),
    /**
     * 三号温度
     */
    qualityTemThree("三号温度", PropertyConstants.TYPE_HUMP_WM_QUALITYTEMTHREE, PropertyConstants.TYPE_WM_QUALITYTEMTHREE, "°C"),
    /**
     * 四号温度
     */
    qualityTemFour("四号温度", PropertyConstants.TYPE_HUMP_WM_QUALITYTEMFOUR, PropertyConstants.TYPE_WM_QUALITYTEMFOUR, "°C"),
    /**
     * 含氯值
     */
    qualityChlorine("含氯值", PropertyConstants.TYPE_HUMP_WM_QUALITYCHLORINE, PropertyConstants.TYPE_WM_QUALITYCHLORINE, "mg/L"),
    /**
     * 含氧值
     */
    qualityOxygen("含氧值", PropertyConstants.TYPE_HUMP_WM_QUALITYOXYGEN, PropertyConstants.TYPE_WM_QUALITYOXYGEN, "mg/L"),
    /**
     * ph值
     */
    qualityPh("ph值", PropertyConstants.TYPE_HUMP_WM_QUALITYPH, PropertyConstants.TYPE_WM_QUALITYPH, ""),

    /**
     * 浑浊度
     */
    qualityTurbid("浑浊度", PropertyConstants.TYPE_HUMP_WM_QUALITYTURBID, PropertyConstants.TYPE_WM_QUALITYTURBID, "NTU"),

    slope("倾斜", PropertyConstants.TYPE_HUMP_HYDRANT_SLOPE, PropertyConstants.TYPE_HYDRANT_SLOPE, "°"),

    highLowAlarm("高低压报警", PropertyConstants.TYPE_HUMP_HYDRANT_HIGH_LOW_ALARM, PropertyConstants.TYPE_HYDRANT_HIGH_LOW_ALARM, ""),

    bump("碰撞", PropertyConstants.TYPE_HUMP_HYDRANT_BUMP, PropertyConstants.TYPE_HYDRANT_BUMP, "");


    private String name;
    private String code;
    private String lowcode;
    private String unit;

    FieldTypeEnum(String name, String code, String lowcode, String Unit) {
        this.name = name;
        this.code = code;
        this.lowcode = lowcode;
        this.unit = Unit;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getLowcode() {
        return lowcode;
    }

    public String getUnit() {
        return this.unit;
    }


    /**
     * 获取 数据库字段和单位的map
     *
     * @return
     */
    public final static Map<String, String> getFiledMap() {
        Map<String, String> filedNameMap = new HashMap<>();
        for (FieldTypeEnum e : values()) {
            filedNameMap.put(e.code, e.unit);
        }
        return filedNameMap;
    }
}
