package com.dotop.smartwater.view.server.core.brustpipe.vo;

import lombok.Data;

import java.util.Date;

@Data
public class BrustPipeOperationsVo {

    /**
     * id主键
     */
    private String id;
    /**
     * 运维周期
     */
    private Integer operationsTime;
    /**
     * 运维次数
     */
    private Integer operationsNum;
    /**
     * 运维负责人
     */
    private String operationsPeole;

    /**
     * 对应的工单的状态
     */
    private String status;

    private String brustPipeId;

    private String workOrderId;

    private Date workOrderCreateDate;

    private String workOrderstatus;

    private String recordData;

    private String processId;

    private Integer offset;
    private Integer limit;
    private Date curr;
    private String userBy;
    private Integer isDel;
    private Integer newIsDel;
    private Boolean ifSort;

}
