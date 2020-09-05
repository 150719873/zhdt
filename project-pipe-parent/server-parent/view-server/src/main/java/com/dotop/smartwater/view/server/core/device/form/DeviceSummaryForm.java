package com.dotop.smartwater.view.server.core.device.form;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 大屏设备统计
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceSummaryForm extends BasePipeForm {

    /**
     * id主键
     */
    private String id;
    /**
     * 数据日期只使用（年月份）
     */
    private Date summaryDate;
    /**
     * 统计类别
     */
    private String summaryCategory;
    /**
     * 统计类型
     */
    private String summaryType;

    private List<String> summaryTypes;
    /**
     * 统计数值（总行度）
     */
    private Double val;
    /**
     * 名称（暂时只有区域用到）
     */
    private String name;
    /**
     * 压力值
     */
    private Double pressureValue;
    /**
     * 设备id（只有传感器有设备id）
     */
    private String deviceId;
    /**
     * 区域编号,用于唯一性约束
     */
    private String areaCode;
    /**
     * 数据密度(常量DATA_DENSITY_DAY:每日一条，DATA_DENSITY_MONTH：每月一条）
     */
    private String dataDensity;

    private Integer offset;
    private Integer limit;
    private Date curr;
    private String userBy;
    private Integer isDel;
    private Integer newIsDel;
    private Boolean ifSort;
    private String group; // year month  weeek
    private Date startDate;
    private Date endDate;
}
