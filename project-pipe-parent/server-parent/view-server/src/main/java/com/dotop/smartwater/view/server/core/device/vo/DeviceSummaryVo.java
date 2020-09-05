package com.dotop.smartwater.view.server.core.device.vo;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 大屏设备统计
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceSummaryVo extends BasePipeVo {

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
    /**
     * 统计数值
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
    // 分组后的 求和
    private String sumval;
    // 分组后求平局值
    private String avgval;

    private String maxval;

    private String groupDate;

    private DeviceVo device;

    Map<String, Map<String, DeviceSummaryVo>> currData;

    public BigDecimal getMaxBigDecimal() {
        if (getMaxval() == null) {
            return new BigDecimal(0);
        } else {
            return new BigDecimal(getMaxval());
        }
    }
}
