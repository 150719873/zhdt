package com.dotop.smartwater.project.third.concentrator.core.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
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
public class DownLinkTaskLogBo extends BaseBo {

    /**
     * 主键
     */
    private String id;

    /**
     * 集中器
     */
    private ConcentratorBo concentrator;

    /**
     * 水表
     */
    private ConcentratorDeviceBo concentratorDevice;

    /**
     * 设备类型(常量)
     */
    private String type;

    /**
     * 任务类型(常量)
     */
    private String taskType;

    /**
     * 任务类型列表(常量)
     */
    private List<String> taskTypes;

    /**
     * 结果(常量)
     */
    private String result;

    /**
     * 下发时间
     */
    private Date deliveryDate;

    /**
     * 完成时间
     */
    private Date completeDate;

    /**
     * 传输参数：json
     */
    private String callableParam;

    /**
     * 返回数据：json
     */
    private String resultData;

    /**
     * 成功数量
     */
    private String successNum;

    /**
     * 失败数量
     */
    private String failNum;

    /**
     * 描述
     */
    private String desc;
    /**
     * 用于查询deliveryDate,初始时间
     */

    private Date startDate;
    /**
     * 用于查询deliveryDate，结束时间
     */
    private Date endDate;
}
