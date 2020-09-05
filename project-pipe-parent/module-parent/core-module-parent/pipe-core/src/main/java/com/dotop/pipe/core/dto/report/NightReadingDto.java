package com.dotop.pipe.core.dto.report;


import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
public class NightReadingDto extends BasePipeDto {

    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 用于查询的设备id
     */
    private List<String> deviceIds;
    /**
     * 设备编号
     */
    private String code;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 区域名称
     */
    private String area;
    /**
     * 读数对比日期
     */
    private String readDate;
    /**
     * 分表日期
     */
    private Set<String> ctimes;
    /**
     * 区域id
     */
    private String areaId;
    /**
     * 查询开始时间
     */
    private String startDate;
    /**
     * 查询结束时间
     */
    private String endDate;
    /**
     * 夜间开始时间
     */
    private String nightStartDate;
    /**
     * 夜间结束时间
     */
    private String nightEndDate;
    /**
     * 天总用水量
     */
    private Double totalWater;
    /**
     * 天夜间总用水量
     */
    private Double nightWater;
}
