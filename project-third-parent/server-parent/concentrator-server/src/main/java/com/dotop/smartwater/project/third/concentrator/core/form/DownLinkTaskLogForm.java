package com.dotop.smartwater.project.third.concentrator.core.form;

import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorBo;
import com.dotop.smartwater.project.third.concentrator.core.bo.ConcentratorDeviceBo;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 下发任务
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DownLinkTaskLogForm extends BaseForm {

    /**
     * 主键
     */
    private String id;

    /**
     * 设备类型(常量)
     */
    private String type;

    /**
     * 集中器
     */
    private ConcentratorBo concentrator;

    /**
     * 水表
     */
    private ConcentratorDeviceBo concentratorDevice;

    /**
     * 结果(常量)
     */
    private String result;


    /**
     * 任务类型(常量)
     */
    private String taskType;

    /**
     * 任务类型列表(常量)
     */
    private List<String> taskTypes;

    /**
     * 下发时间
     */
    private Date deliveryDate;

    /**
     * 完成时间
     */
    private Date completeDate;
    /**
     * 用于查询deliveryDate,初始时间
     */

    private Date startDate;
    /**
     * 用于查询deliveryDate，结束时间
     */
    private Date endDate;
}
