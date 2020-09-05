package com.dotop.smartwater.view.server.core.security.vo;

import com.dotop.smartwater.view.server.core.enums.SecurityEnum;
import com.dotop.smartwater.view.server.core.enums.WaterFactoryPondEnum;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecuritySwitchVo extends BasePipeVo {
    private String id;
    private Boolean status;
    private SecurityEnum address;
    private String facilityId;
    private WaterFactoryPondEnum pond;
}
