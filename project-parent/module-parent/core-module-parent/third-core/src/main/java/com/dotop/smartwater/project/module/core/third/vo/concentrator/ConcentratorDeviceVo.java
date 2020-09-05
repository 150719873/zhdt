package com.dotop.smartwater.project.module.core.third.vo.concentrator;

import lombok.Data;

/**
 * 第三方调用获取水表、集中器、采集器信息获取
 *

 */
@Data
public class ConcentratorDeviceVo {


    /**
     * 集中器
     */
    private String concentratorId;

    /**
     * 集中器
     */
    private String concentratorCode;

    /**
     * 采集器
     */
    private String collectorId;

    /**
     * 采集器
     */
    private String collectorCode;
}
