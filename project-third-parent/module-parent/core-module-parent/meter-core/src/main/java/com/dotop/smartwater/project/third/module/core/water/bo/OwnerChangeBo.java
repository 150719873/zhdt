package com.dotop.smartwater.project.third.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerChangeBo extends BaseBo {

    /**
     * 业主ID
     */
    private String ownerid;
    /**
     * 新设备ID
     */
    private String devid;
    /**
     * 旧设备ID
     */
    private String oldDevid;
}
