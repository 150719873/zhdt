package com.dotop.smartwater.view.server.core.monitor.vo;

import com.dotop.smartwater.view.server.core.enums.WaterFactoryPondEnum;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PondAlarmVo extends BasePipeVo {
    private String id;
    private String type;
    private String status;
    private WaterFactoryPondEnum pond;
    private String facilityId;
    private Date curr;
    private String userBy;
}
