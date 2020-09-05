package com.dotop.smartwater.project.module.core.auth.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlatformRoleForm extends BaseForm {
    private String proleid;

    private String name;

    private String description;

    private String createuser;

    private Date createtime;

}
