package com.dotop.pipe.data.receiver.core.constants;

public class FieldConstants {

    // 流量计编码
    public static final String FLW_CODE = "flwCode";

    // 流量计速率
    public static final String FLW_RATE = "flwRate";
    // 流量计行度
    public static final String FLW_MEASURE = "flwMeasure";
    // 流量计总行度
    public static final String FLW_TOTAL_VALUE = "flwTotalValue";

    // 设备发送时间
    public static final String FLW_SEND_DATE = "flwSendDate";

    // 压力计编码
    public static final String PRESSURE_CODE = "pressureCode";

    // 压力计压力
    public static final String PRESSURE_RATE = "pressureRate";

    // 设备发送时间
    public static final String PRESSURE_SEND_DATE = "pressureSendDate";

    // 水质计
    public static final String QUALITY_CODE = "qualityCode";
    public static final String QUALITY_PH = "qualityPh";
    public static final String QUALITY_OXYGEN = "qualityOxygen";
    public static final String QUALITY_TURBID = "qualityTurbid";
    public static final String QUALITY_CHLORINE = "qualityChlorine";
    public static final String QUALITY_TEM_ONE = "qualityTemOne";
    public static final String QUALITY_TEM_TWO = "qualityTemTwo";
    public static final String QUALITY_TEM_THREE = "qualityTemThree";
    public static final String QUALITY_TEM_FOUR = "qualityTemFour";
    public static final String QUALITY_SEND_DATE = "qualitySendDate";

    // 传感器类型 流量计
    public static final String SENSOR_FLW = "FM";
    // 传感器类型 压力计
    public static final String SENSOR_PRESSURE = "PM";
    // 传感器 水质计
    public static final String SENSOR_QUALITY = "WM";

    /**
     * KBL 设备参数
     */
    // 水质计 ph值
    public static final String KBL_QUALITY_PH = "quality_ph";
    // 水质计 含氧值
    public static final String KBL_QUALITY_OXYGEN = "quality_oxygen";
    // 水质计 浑浊度
    public static final String KBL_QUALITY_TURBID = "quality_turbid";
    // 水质计 含氯值
    public static final String KBL_QUALITY_CHLORINE = "quality_chlorine";
    // 水质计 一号温度
    public static final String KBL_QUALITY_TEM_ONE = "quality_tem_one";
    // 水质计 二号温度
    public static final String KBL_QUALITY_TEM_TWO = "quality_tem_two";
    // 水质计 三号温度
    public static final String KBL_QUALITY_TEM_THREE = "quality_tem_three";
    // 水质计 四号温度
    public static final String KBL_QUALITY_TEM_FOUR = "quality_tem_four";
    // 压力计值
    public static final String KBL_PRESSURE_VALUE = "pressure_value";
    // 流量计 行度
    public static final String KBL_FLW_MEASURE = "flw_measure";
    // 流量计 流速
    public static final String KBL_FLW_RATE = "flw_rate";
    // 流量计 总行度
    public static final String KBL_FLW_TOTAL_VALUE = "flw_total_value";

    // 消防栓 倾斜度
    public static final String KBL_HYDRANT_SLOPE = "slope";
    // 消防栓 高低压报警
    public static final String KBL_HYDRANT_HIGH_LOW_ALARM = "high_low_alarm";
    // 消防栓 碰撞
    public static final String KBL_HYDRANT_BUMP = "bump";

    // 设备编码 -- 通用(水质计 压力计 流量计)
    public static final String KBL_DEVICE_CODE = "device_code";
    // 设备发送时间-- 通用(水质计 压力计 流量计)
    public static final String KBL_DEVICE_SEND_DATE = "dev_send_date";


}
