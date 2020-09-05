package com.dotop.pipe.core.form;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class RuningDataForm extends BasePipeForm {

    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 任务描述
     */
    private String taskDes;
    /***
     * 任务开始时间
     */
    private Date startDate;
    private Date nextStartDate;
    /***
     * 任务结束时间
     */
    private Date endDate;
    /***
     * 定时任务执行周期
     */
    private Integer interval;

    /***
     * 定时任务类型  once 执行一次  repeatedly 多次
     */
    private String type;

    /**
     * 定时任务执行的对象设备
     */
    private List<String> deviceIds;

    /**
     * 设备类型
     */
    private String productCategory;

    /**
     * 设备类别
     */
    private String productType;

    private String status;

    private String enterpriseName;
}
