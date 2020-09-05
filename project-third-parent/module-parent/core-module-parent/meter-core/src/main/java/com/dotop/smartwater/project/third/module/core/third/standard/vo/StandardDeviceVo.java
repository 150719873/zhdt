package com.dotop.smartwater.project.third.module.core.third.standard.vo;


import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StandardDeviceVo extends BaseVo {

    /**
     * 气表编号
     */
    private String devno;

    /**
     * 设备编号
     */
    private String deveui;

    /**
     * 地址
     */
    private String devaddr;

    /**
     * 阀门状态
     */
    private Integer tapStatus;
}
