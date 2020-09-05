package com.dotop.smartwater.project.third.concentrator.core.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 终端返回抄表读数
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TerminalMeterReadBo extends BaseBo {

    /**
     * 集中器编号
     */
    private String concentratorCode;

    /**
     * 序号
     */
    private String no;

    /**
     * 读数
     */
    private String meter;

    /**
     * 接收时间
     */
    private Date receiveDate;

    /**
     * 是否带阀(water常量)；1，带阀；0，不带阀
     */
    private String valve;

    /**
     * 阀门状态 open or close
     */
    private String valveStatus;

    /**
     * 上报是否成功(常量)
     */
    private String result;
}
