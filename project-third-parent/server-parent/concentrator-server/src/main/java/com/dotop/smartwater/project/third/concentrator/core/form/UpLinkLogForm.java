package com.dotop.smartwater.project.third.concentrator.core.form;

import com.dotop.smartwater.project.third.concentrator.core.bo.CollectorBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorDeviceBo;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 上行日志
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UpLinkLogForm extends BaseForm {

    /**
     * 主键
     */
    private String id;
    /**
     * 集中器
     */
    private ConcentratorBo concentrator;

    /**
     * 采集器
     */
    private CollectorBo collector;

    /**
     * 水表
     */
    private ConcentratorDeviceBo concentratorDevice;
    /**
     * 原始数据
     */
    private String oriData;
    /**
     * 接收时间
     */
    private Date receiveDate;

    /**
     * 读数
     */
    private String water;

    /**
     * 抄表结果(常量)
     */
    private String result;

    /**
     * 执行任务id
     */
    private String taskLogId;

    /**
     *  用于查询receiveDate,初始时间
     */

    private Date startDate;
    /**
     *  用于查询receiveDate，结束时间
     */
    private Date endDate;

    /**
     * 正常或者异常，仅供跳变使用
     */
    private Integer status;
}
