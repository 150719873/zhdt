package com.dotop.smartwater.project.third.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class CommandForm extends BaseForm {
    /**
     * 主键id
     */
    private String id;

    /**
     * 设备编号
     */
    private String devno;

    /**
     * 任务id
     */
    private String clientid;

    /**
     * 结果，0失败1成功
     */
    private String result;

    /**
     * 状态，0未处理，1处理中，2完成，3撤销
     */
    private Integer status;

    /**
     * 描述
     */
    private String des;

    /**
     * 设备id
     */
    private String devid;

    /**
     * 设备编号
     */
    private String deveui;

    /**
     * 任务类型
     */
    private Integer command;

    /**
     * 上报周期
     */
    private String cycle;

    /**
     * 水表读数
     */
    private Double water;

}
