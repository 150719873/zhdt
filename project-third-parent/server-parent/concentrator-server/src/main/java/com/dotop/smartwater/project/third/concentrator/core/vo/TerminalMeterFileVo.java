package com.dotop.smartwater.project.third.concentrator.core.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 终端返回集中器档案
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TerminalMeterFileVo extends BaseVo {

    /**
     * 集中器编号
     */
    private String concentratorCode;

    /**
     * 序号
     */
    private String no;

    /**
     * 采集器编号
     */
    private String collectorCode;

    /**
     * 水表编号
     */
    private String deviceCode;
}
