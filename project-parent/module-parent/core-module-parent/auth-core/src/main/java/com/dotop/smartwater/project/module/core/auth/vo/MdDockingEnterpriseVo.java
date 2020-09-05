package com.dotop.smartwater.project.module.core.auth.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MdDockingEnterpriseVo extends BaseVo {
    private String id;
    private String enterpriseid;
    private String code;
    private String waterHost;
    private String factoryId;
    private Boolean status;
    private String enterpriseName;
}