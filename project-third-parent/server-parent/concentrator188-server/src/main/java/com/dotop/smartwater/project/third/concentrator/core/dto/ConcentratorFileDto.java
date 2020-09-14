package com.dotop.smartwater.project.third.concentrator.core.dto;

import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.dto.DeviceDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 集中器档案
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ConcentratorFileDto extends DeviceDto {

    /**
     * 主键
     */
    private String id;

    /**
     * 集中器编号
     */
    private String concentratorCode;

    /**
     * 采集器表号
     */
    private String collectorCode;

    /**
     * 序号
     */
    private Integer no;

    /**
     * 水表编号
     */
    private String devno;
}
