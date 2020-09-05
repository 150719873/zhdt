package com.dotop.smartwater.project.module.core.auth.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class MdDockingEnterpriseBo extends BaseBo {
    private String id;
    private String enterpriseid;
    private String code;
    private String waterHost;
    private String factoryId;
    private Boolean status;
    private String createBy;
    private Date createDate;
    private String lastBy;
    private Date lastDate;

}