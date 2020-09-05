package com.dotop.smartwater.project.third.concentrator.core.dto;


import com.dotop.smartwater.dependence.core.common.BaseDto;
import com.dotop.smartwater.project.third.concentrator.core.bo.CollectorBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorDeviceBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class UpLinkLogDto extends BaseDto {

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
     * 用于查询receiveDate,初始时间
     */

    private Date startDate;
    /**
     * 用于查询receiveDate，结束时间
     */
    private Date endDate;

    /**
     * 用于分表查询
     */

    private String preMonth;
    /**
     * 用于分表查询
     */
    private String thisMonth;
}
