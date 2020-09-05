package com.dotop.pipe.core.bo.patrol;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class PatrolRouteTemplateBo extends BasePipeBo {
    /**
     * 巡检路线id
     */
    private String patrolRouteTemplateId;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String desc;

    /**
     * 用于查询时间,初始时间
     */

    private String startDateTemp;
    /**
     * 用于查询时间，结束时间
     */
    private String endDateTemp;

    /**
     * 路线点的id集合
     */
    private List<String> patrolRoutePointIds = new ArrayList<>();

    /**
     * 路线点的集合
     */
    private List<PatrolRoutePointBo> patrolRoutePoints = new ArrayList<>();
}
