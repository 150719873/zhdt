package com.dotop.smartwater.view.server.core.workorder.vo;

import lombok.Data;

import java.util.Date;

@Data
public class WorkOrderSummaryVo {

    /**
     * id主键
     */
    private String id;
    /**
     * 统计类型
     */
    private String type;
    /**
     * 统计状态
     */
    private String status;
    /**
     * 统计数值
     */
    private Integer count;



    private Integer offset;
    private Integer limit;
    private Date curr;
    private String userBy;
    private Integer isDel;
    private Integer newIsDel;
    private Boolean ifSort;

}
