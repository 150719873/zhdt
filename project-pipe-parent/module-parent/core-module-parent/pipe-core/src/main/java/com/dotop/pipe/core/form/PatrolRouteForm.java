package com.dotop.pipe.core.form;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class PatrolRouteForm extends BasePipeForm {
    /**
     * 巡检路线id
     */
    private String patrolRouteId;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 巡检任务的id集合
     */
    private List<String> taskIds = new ArrayList<>();

    /**
     * 巡检任务的集合
     */
    private List<PatrolRouteTaskForm> tasks = new ArrayList<>();

    /**
     * 巡检路线的id集合
     */
    private List<String> patrolRoutePointIds = new ArrayList<>();

    /**
     * 巡检路线的集合
     */
    private List<PatrolRoutePointForm> patrolRoutePoints = new ArrayList<>();

    /**
     * 描述
     */
    private String desc;

    /**
     * 原因
     */
    private String reason;

    /**
     * 用于查询时间,初始时间
     */

    private String startDateTemp;
    /**
     * 用于查询时间，结束时间
     */
    private String endDateTemp;

    private String patrolTaskId;


}
