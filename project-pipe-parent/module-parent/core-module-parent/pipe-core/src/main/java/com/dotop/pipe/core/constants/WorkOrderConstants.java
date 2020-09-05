package com.dotop.pipe.core.constants;

public class WorkOrderConstants {

    // 工单类别
    // 维修工单
    public final static String ORDER_CATEGORY_MAINTENANCE = "maintenance";
    // 施工工单
    public final static String ORDER_CATEGORY_CONSTRUCTION = "construction";

    // 工单来源
    // web手工
    public final static String ORDER_SOURCE_WEB_HANDWORK = "web";
    // 报警
    public final static String ORDER_SOURCE_ALARM = "alarm";

    // 工单状态
    // 工单开启
    public final static String ORDER_STATUS_OPEN = "10";
    // 工单处理
    public final static String ORDER_STATUS_HANDLE = "20";
    // 工单完成
    public final static String ORDER_STATUS_COMPLETE = "30";
    // 工单重处理
    public final static String ORDER_STATUS_REHANDLE = "40";
    // 工单重完成
    public final static String ORDER_STATUS_RECOMPLETE = "50";


    // 工单类型
    // 计划性维护工作流程
    public final static String ORDER_TYPE_PLANNED = "planned_maintenance";
    // 应急性维护管理流程
    public final static String ORDER_TYPE_EMERGENCY = "emergency_maintenance";
    // 管网信息维护管理流程
    public final static String ORDER_TYPE_INFORMATION = "information_maintenance";

    // 工单状态
    // 未处理
    public final static String ORDER_STATUS_UNTREATED = "-2";
    public final static String ORDER_STATUS_APPLYED = "-1";
    // 处理中
    public final static String ORDER_STATUS_PROCESSING = "0";
    // 完成
    public final static String ORDER_STATUS_COMPLETED = "1";

    // 处理结果
    public final static String ORDER_RESULT_NOPASS = "不通过";
    // 通过
    public final static String ORDER_RESULT_PASS = "通过";


    // 抢修：应急性维护管理流程
    public static final String ORDER_TYPE_REPAIR = "repair";
    // 报装：计划性报装工作流程
    public static final String ORDER_TYPE_INSTALL = "install";
    // 抄表：管网信息维护管理流程
    public static final String ORDER_TYPE_READ = "read";
    // 施工：计划性施工工作流程
    public static final String ORDER_TYPE_CONSTRUCTION = "construction";
    // 勘察：计划性勘察工作流程
    public static final String ORDER_TYPE_SURVEY = "survey";


}
