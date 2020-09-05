package com.dotop.smartwater.project.third.module.core.constants;

/**
 *
 */
public class DockingConstants {

    /**
     * 返回结果，成功失败
     */
    public static final String RESULT_SUCCESS = "0";
    public static final String RESULT_FAIL = "1";

    public static final String RESULT_SUCCESS_MSG = "成功";
    public static final String RESULT_FAIL_MSG = "失败";

    /**
     * 下发命令状态, 0未处理，1处理中，2完成，3撤销
     */
    public static final Integer COMMAND_READY = 0;
    public static final Integer COMMAND_DOING = 1;
    public static final Integer COMMAND_FINISH = 2;
    public static final Integer COMMAND_CANCEL = 3;

    /**
     * 收费系统
     */
    public static final String CHARGE = "CHARGE";
    /**
     * 抄表系统
     */
    public static final String METER_READING = "METER_READING";


    //start-----------------------------------------------------------------------------视频直读-------------------------------------------------------------------------------

    /**
     * 类别
     */
    public static final String VIDEO = "VIDEO";

    /**
     * 登录
     */
    public static final String VIDEO_POST_LOGIN = "VIDEO_POST_LOGIN";
    /**
     * 获取客户列表
     */
    public static final String VIDEO_POST_CUSTOMER = "VIDEO_POST_CUSTOMER";
    /**
     * 获取客户资料信息
     */
    public static final String VIDEO_GET_CUSTOMER = "VIDEO_GET_CUSTOMER";
    /**
     * 获取智能水表列表
     */
    public static final String VIDEO_POST_IMETER = "VIDEO_POST_IMETER";
    /**
     * 获取智能是水表信息
     */
    public static final String VIDEO_GET_IMETER = "VIDEO_GET_IMETER";
    /**
     * 获取抄表信息
     */
    public static final String VIDEO_POST_RECORD = "VIDEO_POST_RECORD";
    /**
     * 注销
     */
    public static final String VIDEO_GET_CLEANUP = "VIDEO_GET_CLEANUP";

    //end-------------------------------------------------------------------------------视频直读------------------------------------------------------------------------------


    //start-----------------------------------------------------------------------------鼎通NB-------------------------------------------------------------------------------

    /**
     * 类别
     */
    public static final String DTNB = "DTNB";

    /**
     * 登录
     */
    public static final String DTNB_LOGIN = "DTNB_LOGIN";

    /**
     * 获取设备信息列表
     */
    public static final String DTNB_GET_DEVICE_INFO = "DTNB_GET_DEVICE_INFO";
    /**
     * 获取水表信息
     */
    public static final String DTNB_GET_DEVICE_CURRENT_DATA = "DTNB_GET_DEVICE_CURRENT_DATA";
    /**
     * 获取日冻结数据
     */
    public static final String DTNB_GET_DEVICE_FREEZE_DATA = "DTNB_GET_DEVICE_FREEZE_DATA";
    /**
     * 发送命令
     */
    public static final String DTNB_SEND_COMMAND = "DTNB_SEND_COMMAND";
    /**
     * 撤销命令
     */
    public static final String DTNB_CANCEL_COMMAND = "DTNB_CANCEL_COMMAND";
    /**
     * 查询设备控制状态
     */
    public static final String DTNB_GET_COMMAND_STATUS = "DTNB_GET_COMMAND_STATUS";
    /**
     * 删除设备
     */
    public static final String DTNB_REMOVE_DEVICE_INFO = "DTNB_REMOVE_DEVICE_INFO";


    //end-------------------------------------------------------------------------------鼎通NB------------------------------------------------------------------------------
    //start-----------------------------------------------------------------------------远程表-------------------------------------------------------------------------------

    /**
     * 类别
     */
    public static final String NB2 = "nb2";
    /**
     * 用户信息
     */
    public static final String REMOTE_NB_USER_INFO = "REMOTE_NB_USER_INFO";

    /**
     * 操作类型 1 新增2 修改3开阀4关阀
     */
    public static final String REMOTE_USER_ADD = "1";
    public static final String REMOTE_USER_EDIT = "2";
    public static final String REMOTE_DEVICE_OPEN = "3";
    public static final String REMOTE_DEVICE_CLOSE = "4";
    /**
     * 操作结果, 1成功 2失败
     */
    public static final String REMOTE_SUCCESS = "1";
    public static final String REMOTE_FAIL = "2";

    //end-------------------------------------------------------------------------------远程表------------------------------------------------------------------------------

    //start-----------------------------------------------------------------------------全景智慧水务-------------------------------------------------------------------------------
    /**
     * 登录
     */
    public static final String ZHSW_POST_GETACCESSTOKEN = "ZHSW_POST_GETACCESSTOKEN";

    /**
     * 上传客户信息
     */
    public static final String ZHSW_POST_UPDATECLIENTINFO = "ZHSW_POST_UPDATECLIENTINFO";

    /**
     * 更新水表信息
     */
    public static final String ZHSW_POST_UPDATEWATERMETERRECORD = "ZHSW_POST_UPDATEWATERMETERRECORD";
    //end-------------------------------------------------------------------------------全景智慧水务------------------------------------------------------------------------------
    // 对外接口类型(type)
    public static final String REMOTE_API = "REMOTE_API";

    //start-----------------------------------------------------------------------------combine-------------------------------------------------------------------------------

    // combine
    public static final String KBL_NB_USER_INFO = "KBL_USER_REMOTE";

    //end-----------------------------------------------------------------------------康宝莱-------------------------------------------------------------------------------
    //start-----------------------------------------------------------------------------深圳lora-------------------------------------------------------------------------------
    /**
     * 深圳lora，登录信息
     */
    public static final String HAT_LORA_LOGIN = "HAT_LORA_LOGIN";
    /**
     * 深圳lora，获取水表信息列表
     */
    public static final String HAT_LORA_DEVICE_INFO = "HAT_LORA_DEVICE_INFO";
    /**
     * 深圳lora，改写水表记录状态
     */
    public static final String HAT_LORA_SET_METER_FLAG = "HAT_LORA_SET_METER_FLAG";
    /**
     * 深圳lora，根据水表Id查询水表详细信息
     */
    public static final String HAT_LORA_GET_DEVICE_DETAIL_BY_ID = "HAT_LORA_GET_DEVICE_DETAIL_BY_ID";
    /**
     * 深圳lora，根据水表iAddr查询水表详细信息
     */
    public static final String HAT_LORA_GET_DEVICE_DETAIL_BY_IADDR = "HAT_LORA_GET_DEVICE_DETAIL_BY_IADDR";
    /**
     * 深圳lora，根据水表Id修改水表用户信息
     */
    public static final String HAT_LORA_SET_USER_INFO_BY_ID = "HAT_LORA_SET_USER_INFO_BY_ID";
    /**
     * 深圳lora，根据水表iAddr修改水表用户信息
     */
    public static final String HAT_LORA_SET_USER_INFO_BY_IADDR = "HAT_LORA_SET_USER_INFO_BY_IADDR";
    /**
     * 深圳lora，获取换表记录
     */
    public static final String HAT_LORA_GET_METER_CHANGE = "HAT_LORA_GET_METER_CHANGE";
    /**
     * 深圳lora，改写换表记录状态
     */
    public static final String HAT_LORA_SET_CHANGE_FLAG = "HAT_LORA_SET_CHANGE_FLAG";
    /**
     * 深圳lora，根据水表Id、指定抄表日期获取抄表数据
     */
    public static final String HAT_LORA_GET_METER_VALUE_BY_ID = "HAT_LORA_GET_METER_VALUE_BY_ID";
    /**
     * 深圳lora，根据水表iAddr、指定抄表日期获取抄表数据
     */
    public static final String HAT_LORA_GET_METER_VALUE_BY_IADDR = "HAT_LORA_GET_METER_VALUE_BY_IADDR";
    /**
     * 深圳lora，操作水表开关阀及抄表
     */
    public static final String HAT_LORA_SET_METER_VALUE = "HAT_LORA_SET_METER_VALUE";
    /**
     * 深圳lora，操作水表开关阀
     */
    public static final String HAT_LORA_SET_METER_VALUE_BY_IADDR = "HAT_LORA_SET_METER_VALUE_BY_IADDR";
    /**
     * 深圳lora，查询水表开关阀执行时临时指令队列Id和GenerateTime
     */
    public static final String HAT_LORA_GET_METER_VALUE_AND_GENERATE_TIME_BY_IADDR = "HAT_LORA_GET_METER_VALUE_AND_GENERATE_TIME_BY_IADDR";
    /**
     * 深圳lora，查询水表开关阀执行时临时指令队列State值
     */
    public static final String HAT_LORA_GET_METER_VALUE_STATE = "HAT_LORA_GET_METER_VALUE_STATE";
    /**
     * 深圳lora，广播开关阀
     */
    public static final String HAT_LORA_BROADCAST_VALVE = "HAT_LORA_BROADCAST_VALVE";

    //end-----------------------------------------------------------------------------深圳lora-------------------------------------------------------------------------------
}
