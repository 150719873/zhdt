package com.dotop.smartwater.project.third.module.core.third.standard.vo;


import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UplinkVo extends BaseVo {

    /**
     * 气表编号
     */
    private String devno;

    /**
     * 止度
     */
    private String water;

    /**
     * 抄表时间
     */
    private String uplinkTime;

    /**
     * 针对流量计补充字段
     */


    /**
     * 厂家代码
     */
    private String factory;
    /**
     * 流速，单位升/时
     */
    private String flowRate;
    /**
     * 管道压力，单位：Kpa
     */
    private String pressure;
    /**
     * 累计工作时间，单位：小时
     */
    private String totalWorkTime;
    /**
     * 管道水温，单位：度
     */
    private String temperature;
    /**
     * 水表内部时钟
     */
    private String internalClock;
    /**
     * 异常状态：正常、无水或断线
     */
    private String status;


}
