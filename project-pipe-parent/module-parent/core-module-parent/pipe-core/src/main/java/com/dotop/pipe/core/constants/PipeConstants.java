package com.dotop.pipe.core.constants;

public class PipeConstants {

    // 区域默认
    public final static String AREA_PARENT_CODE = "0";

    // 0:非叶子
    public final static Integer AREA_IS_LEAF = 0;

    // 1:非叶子
    public final static Integer AREA_ISNOT_LEAF = 1;

    // 0 不是父节点
    public final static Integer AREA_ISNOT_PARENT = 0;

    // 1 是父节点
    public final static Integer AREA_IS_PARENT = 1;

    // 区域编码自增长
    public final static String AREA_CODE_ADD = "001";
    public final static String AREA_CODE_ADD_DZERO = "00";
    public final static String AREA_CODE_ADD_SZERO = "0";

    // 分析状态,启用
    public final static String ANALYSIS_STATUS_ENABLE = "1";
    // 分析状态,未启用
    public final static String ANALYSIS_STATUS_DISABLE = "0";
    // 分析类型,选择
    public final static String ANALYSIS_TYPE_SELECT = "select";
    // 分析类型,区域
    public final static String ANALYSIS_TYPE_AREA = "area";
    // 方向+
    public final static String DIRECTION_PLUS = "PLUS";
    // 方向-
    public final static String DIRECTION_SUB = "SUB";

    public final static String PIPE_UNTREATED_STATUS = "0";

    public final static String PIPE_HANDLED_STATUS = "1";

    // 巡检路线任务的状态 0.未开始 1. 已处理
    // 未开始
    public final static String PATROL_ROUTE_TASK_STATUS_NOTSTART = "0";
    // 已处理
    public final static String PATROL_ROUTE_TASK_STATUS_HANDLED = "1";

    // 巡检任务状态 -1.超时 0. 未开始 1. 处理中 2 完成
    // 超时
    public final static String PATROL_TASK_STATUS_OVERTIME = "-1";
    // 未开始
    public final static String PATROL_TASK_STATUS_NOTSTART = "0";
    // 处理中
    public final static String PATROL_TASK_STATUS_HANDLEING = "1";
    //  完成
    public final static String PATROL_TASK_STATUS_COMPLETE = "2";


    // 环比
    public final static String RING_RATIO = "RING_RATIO";
    // 同比
    public final static String YEAR_RATIO = "YEAR_RATIO";

}
