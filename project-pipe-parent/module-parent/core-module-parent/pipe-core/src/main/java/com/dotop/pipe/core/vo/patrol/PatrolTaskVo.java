package com.dotop.pipe.core.vo.patrol;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class PatrolTaskVo extends BasePipeVo {

    /**
     * 巡检任务id
     */
    private String patrolTaskId;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 完成时间
     */
    private Date completionTime;

    /**
     * 状态(常量) -1. 超时  0. 未开始   1. 处理中   2. 完成
     */
    private String status;

    /**
     * 描述
     */
    private String desc;

    /**
     * 原因
     */
    private String reason;

    /**
     * 处理人的id集合
     */
    private List<String> handlerUserIds;

    /**
     * 处理人的集合
     */
    private List<UserVo> handlerUsers;

    /**
     * 任务的id集合
     */
    private List<String> taskIds;

    /**
     * 任务的集合
     */
    private List<PatrolRouteTaskVo> tasks;

    /**
     * 用于查询时间,初始时间
     */

    private String startDateTemp;
    /**
     * 用于查询时间，结束时间
     */
    private String endDateTemp;

    private String showUser;

    private String patrolRouteId;

    private PatrolRouteVo patrolRoute;
}
