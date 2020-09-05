package com.dotop.smartwater.project.module.core.auth.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlatformRoleBo extends BaseBo {
    private String proleid;

    private String name;

    private String description;

    private String createuser;

    private Date createtime;

}
