package com.dotop.smartwater.project.module.core.third.bo.pipe;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;

import java.util.Date;

@Data
public class PipeWorkOrder extends BaseBo {



    /**
     * 工单id
     */
    private String workOrderId;

    /**
     *  编号
     */
    private String code;

    /**
     * 名称
     */
    private String workorderName;

    /**
     * 类型
     *    1.计划性维护工作流程
     *    2.应急性维护管理流程
     *    3.管网信息维护管理流程
     */
    private String type;

    /**
     * 发起时间
     */
    private Date initiationTime;

    /**
     * 状态
     */
    private String status;

    /**
     * 结果
     */
    private String result;

    /**
     * json数据
     */
    private String recordData;

    /**
     * 用于查询initiationTime,初始时间
     */

    private String startDate;
    /**
     * 用于查询initiationTime，结束时间
     */
    private String endDate;
    /**
     * 为了对应管漏的企业id
     */
    private String enterpriseId;

    /**
     * 流程id
     */
    private String processId;
}
