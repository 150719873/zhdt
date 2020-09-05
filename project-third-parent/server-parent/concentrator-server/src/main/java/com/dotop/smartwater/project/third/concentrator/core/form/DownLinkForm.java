package com.dotop.smartwater.project.third.concentrator.core.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 下发命令
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DownLinkForm extends BaseForm {

    /**
     * 集中器
     */
    private ConcentratorForm concentrator;

    /**
     * 任务类型(常量)
     */
    private String taskType;

    /**
     * 设置-集中器时间
     */
    private Date setDate;

    /**
     * 设置-上报时间
     */
    private Date setUplinkDate;
    /**
     * 设置-上报时间类型(常量)
     */
    private String setUplinkDateType;

    /**
     * 设置-是否允许数据上报(常量)
     */
    private String setIsAllowUplinkData;

    /**
     * 设置-gprs主ip
     */
    private String setGprsIp;
    /**
     * 设置-gprs主端口
     */
    private String setGprsPort;

    /**
     * 集中器的水表编号
     */
    private String devno;


    /**
     * 开关阀操作(常量)
     */
    private String valveOper;
}
