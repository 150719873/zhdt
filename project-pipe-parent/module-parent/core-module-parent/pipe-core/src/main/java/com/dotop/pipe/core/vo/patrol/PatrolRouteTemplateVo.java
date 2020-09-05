package com.dotop.pipe.core.vo.patrol;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class PatrolRouteTemplateVo extends BasePipeVo {
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
    private List<PatrolRoutePointVo> patrolRoutePoints = new ArrayList<>();
}
