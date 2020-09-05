package com.dotop.pipe.core.bo.workOrder;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class WorkOrderBo  extends BasePipeBo {

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
    private String workOrderName;



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
     * 流程id
     */
    private String processId;

    /**
     * 类型
     *   repair 抢修：应急性维护管理流程
     *  install 报装：计划性报装工作流程
     *  read 抄表：管网信息维护管理流程
     *  construction 施工：计划性施工工作流程
     *  survey 勘察：计划性勘察工作流程
     */
    private String type;
}
