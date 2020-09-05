package com.dotop.pipe.core.vo.patrol;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class PatrolRouteTaskVo extends BasePipeVo {

    /**
     * 巡检路线的任务id
     */
    private String patrolRouteTaskId;

    /**
     * 任务标题
     */
    private String taskTitle;

    /**
     * 任务内容
     */
    private String taskContent;

    /**
     * 任务排序
     */
    private String sort;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 任务描述
     */
    private String desc;

    /**
     * 任务点经度
     */
    private String longitude;

    /**
     *任务点纬度
     */
    private String latitude;

    /**
     * 最后处理人id
     */
    private String lastHandlerUserId;

    /**
     * 最后处理人
     */
    private UserVo user;

    /**
     * 最后处理时间
     */
    private Date lastHandleTime;

    /**
     * 工作内容
     */
    private String workContent;

    /**
     * 用于查询时间,初始时间
     */

    private String startDateTemp;
    /**
     * 用于查询时间，结束时间
     */
    private String endDateTemp;

    private List<String> patrolRouteTaskIds;

}
