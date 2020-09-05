package com.dotop.smartwater.view.server.core.monitor.form;

import com.dotop.smartwater.view.server.core.enums.WaterFactoryPondEnum;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LiquidLevelForm extends BasePipeForm {
    private String id;
    private String val;
    private String status;
    private WaterFactoryPondEnum pond;
    private String facilityId;
    private Date curr;
    private String userBy;
}
