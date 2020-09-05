package com.dotop.pipe.core.form;

import com.dotop.pipe.core.enums.CycleTemplateEnum;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class PatrolTaskTimerForm extends BasePipeForm {

    /**
     * 定时任务id
     */
    private String patrolTaskTimerId;

    /**
     * 绑定的路线id
     */
    private String patrolRouteId;

    /**
     * 绑定的路线
     */
    private PatrolRouteForm patrolRoute;

    /**
     * 处理人的id集合
     */
    private List<String> handlerUserIds;

    /**
     * 用于查询时间,初始时间
     */

    private Date startDate;

    /**
     * 用于查询时间，结束时间
     */
    private Date endDate;

    /**
     * 定时任务起始日期
     */
    private Date basicTime;

    /**
     * 最后执行日期
     */
    private Date lastTime;

    /**
     * 期限(天)
     */
    private Integer limit;

    /**
     * 周期(天)
     */
    private Integer cycle;

    /**
     * 发起间隔
     */
    private CycleTemplateEnum cycleTemplate;

    /**
     * 已执行次数
     */
    private Integer runTimes;

    /**
     * 状态(0：执行，1：不执行)
     */
    private Integer status;

    /**
     * 限制执行次数
     */
    private Integer limitTimes;

    /**
     * 是否筛选未执行完的任务
     */
    private Boolean runTimesFlag;
}
