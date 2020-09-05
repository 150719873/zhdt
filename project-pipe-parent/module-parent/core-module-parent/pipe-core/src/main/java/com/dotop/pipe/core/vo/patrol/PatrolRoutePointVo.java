package com.dotop.pipe.core.vo.patrol;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PatrolRoutePointVo extends BasePipeVo {

    /**
     * 巡检路线id
     */
    private String patrolRoutePointId;

    /**
     * 巡检路线点经度
     */
    private String longitude;

    /**
     * 巡检路线点纬度
     */
    private String latitude;
}
