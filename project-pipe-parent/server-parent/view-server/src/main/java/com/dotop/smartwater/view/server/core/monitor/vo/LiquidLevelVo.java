package com.dotop.smartwater.view.server.core.monitor.vo;

import com.dotop.smartwater.view.server.core.enums.WaterFactoryPondEnum;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LiquidLevelVo extends BasePipeVo {
    private String id;
    private String val;
    private String max;
    private String min;
    private String status;
    private WaterFactoryPondEnum pond;
    private String facilityId;
    private Date curr;
    private String userBy;

    public void setPond(WaterFactoryPondEnum pond) {
        this.pond = pond;
        this.max = pond.getMax();
        this.min = pond.getMin();
    }
}
