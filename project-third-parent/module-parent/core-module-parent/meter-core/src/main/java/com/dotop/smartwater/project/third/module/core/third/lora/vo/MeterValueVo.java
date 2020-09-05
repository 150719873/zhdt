package com.dotop.smartwater.project.third.module.core.third.lora.vo;


import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class MeterValueVo extends BaseVo {

    /**
     * 水表读数
     */
    private String meterValue;

    /**
     * 水表状态：0-正常 1-读表失败
     */
    private Integer meterState;

    /**
     * 水表状态：正常  读表失败
     */
    private String mState;
    /**
     * 水表编号
     */
    private String meterNo;
    /**
     * 集中器编号
     */
    private String centerNo;
    /**
     * 采集器编号
     */
    private String collectionNo;
    /**
     * 用户编号
     */
    private String userNo;
    /**
     * 抄表时间
     */
    private Date readTime;
    /**
     * 用户编号
     */
    private String userName;
    /**
     * 备注
     */
    private String remark;
}
