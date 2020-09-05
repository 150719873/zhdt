package com.dotop.smartwater.project.third.concentrator.core.constants;

/**
 * 集中器常量
 *
 *
 */
public class ConcentratorConstants {

    /**
     * 130协议特殊68版本
     */
    public static final String PROTOCOL_130_68 = "130_68";
    /**
     * -----------------------------------------------------
     */
    /**
     * 阀门状态：开阀状态
     */
    public static final String VALVE_STATUS_OPEN = "OPEN";
    /**
     * 阀门状态：关阀状态
     */
    public static final String VALVE_STATUS_CLOSE = "CLOSE";
    /**
     * -----------------------------------------------------
     */
    /**
     * 阀门操作：开阀
     */
    public static final String VALVE_OPER_OPEN = "1";
    /**
     * 阀门操作：关阀
     */
    public static final String VALVE_OPER_CLOSE = "0";
    /**
     * -----------------------------------------------------
     */
    /**
     * 是否需要重新排序：需要
     */
    public static final Integer NEED_REORDERING = 1;
    /**
     * 是否需要重新排序：不需要
     */
    public static final Integer NOT_NEED_REORDERING = 0;
    /**
     * -----------------------------------------------------
     */
    /**
     * 是否允许上报：允许
     */
    public static final String ALLOW_UPLINK = "1";
    /**
     * 是否允许上报：不允许
     */
    public static final String ALLOW_NO_UPLINK = "0";
    /**
     * -----------------------------------------------------
     */
    /**
     * 上报状态类型
     */
    public static final String UPLOAD_TYPE_MONTH = "C1";
    public static final String UPLOAD_TYPE_MONTH_NAME = "每月上报";

    public static final String UPLOAD_TYPE_DAY = "81";
    public static final String UPLOAD_TYPE_DAY_NAME = "每日上报";

    public static final String UPLOAD_TYPE_HOUR = "41";
    public static final String UPLOAD_TYPE_HOUR_NAME = "每小时上报";

    public static final String UPLOAD_TYPE_MINUTE = "CF";
    public static final String UPLOAD_TYPE_MINUTE_NAME = "每15分钟上报";
    /**
     * -----------------------------------------------------
     */

    /**
     * 状态：在用
     */
    public static final String STATUS_USE = "USE";
    /**
     * 状态：停用
     */
    public static final String STATUS_DISABLE = "DISABLE";
    /**
     * -----------------------------------------------------
     */
    /**
     * 是否在线：在线
     */
    public static final String ONLINE_ONLINE = "ONLINE";
    /**
     * 是否在线：下线
     */
    public static final String ONLINE_OFFLINE = "OFFLINE";
    /**
     * -----------------------------------------------------
     */
    /**
     * 结果：成功
     */
    public static final String RESULT_SUCCESS = "SUCCESS";
    /**
     * 结果：等待
     */
    public static final String RESULT_WAIT = "WAIT";
    /**
     * 结果：失败
     */
    public static final String RESULT_FAIL = "FAIL";
    /**
     * -----------------------------------------------------
     */
    /**
     * 设备类型：集中器
     */
    public static final String TYPE_CONCENTRATOR = "CONCENTRATOR";
    /**
     * 设备类型：采集器
     */
    public static final String TYPE_COLLECTOR = "COLLECTOR";
    /**
     * 设备类型：集中器设备
     */
    public static final String TYPE_CONCENTRATOR_DEVICE = "CONCENTRATOR_DEVICE";
    /**
     * -----------------------------------------------------
     */
    /**
     * 在线状态检查
     */
    public static final String TASK_TYPE_HEARTBEAT = "HEARTBEAT";
    /**
     * 下载档案
     */
    public static final String TASK_TYPE_DOWNLOAD_FILE = "DOWNLOAD_FILE";
    /**
     * 读取档案
     */
    public static final String TASK_TYPE_READ_FILE = "READ_FILE";
    /**
     * 全部抄表
     */
    public static final String TASK_TYPE_ALL_METER_READ = "ALL_METER_READ";
    /**
     * 读取-是否允许数据上报
     */
    public static final String TASK_TYPE_READ_IS_ALLOW_UPLINK_DATA = "READ_IS_ALLOW_UPLINK_DATA";
    /**
     * 设置-是否允许数据上报
     */
    public static final String TASK_TYPE_SET_IS_ALLOW_UPLINK_DATA = "SET_IS_ALLOW_UPLINK_DATA";
    /**
     * 读取-上报时间
     */
    public static final String TASK_TYPE_READ_UPLINK_DATE = "READ_UPLINK_DATE";
    /**
     * 设置-上报时间
     */
    public static final String TASK_TYPE_SET_UPLINK_DATE = "SET_UPLINK_DATE";
    /**
     * 读取-网络gprs设置
     */
    public static final String TASK_TYPE_READ_GPRS = "READ_GPRS";
    /**
     * 设置-网络gprs设置
     */
    public static final String TASK_TYPE_SET_GPRS = "SET_GPRS";
    /**
     * 读取-集中器时间
     */
    public static final String TASK_TYPE_READ_DATE = "READ_DATE";
    /**
     * 设置-集中器时间
     */
    public static final String TASK_TYPE_SET_DATE = "SET_DATE";
    /**
     * 单表抄表
     */
    public static final String TASK_TYPE_METER_READ = "METER_READ";
    /**
     * 自动上报水表信息
     */
    public static final String TASK_TYPE_AUTO_UPLOAD = "AUTO_UPLOAD";
    /**
     * 初始化数据
     */
    public static final String TASK_TYPE_DATA_INITIALIZATION = "DATA_INITIALIZATION";
    /**
     * 开关阀：开阀
     */
    public static final String TASK_TYPE_DEVICE_OPEN = "DEVICE_VALVE_OPEN";
    /**
     * 开关阀：关阀
     */
    public static final String TASK_TYPE_DEVICE_CLOSE = "DEVICE_VALVE_CLOSE";


    /**
     * 运营商：中国移动
     */
    public static final String OPERATOR_CHINA_MOBILE = "CHINA_MOBILE";

    /**
     * 运营商：中国联通
     */
    public static final String OPERATOR_CHINA_UNICOM = "CHINA_UNICOM";

    /**
     * 运营商：中国电信
     */
    public static final String OPERATOR_CHINA_TELECOM = "CHINA_TELECOM";

    /**
     * 跳变状态：正常
     */
    public static final Integer DEVICE_NORMAIL = 1;

    /**
     * 跳变状态：异常
     */
    public static final Integer DEVICE_ABNORMAIL = 0;

    /**
     * 采集器编号，及末尾0
     */
    public static final String COLLECTOR_ABSTRACT = "00000000";
}
