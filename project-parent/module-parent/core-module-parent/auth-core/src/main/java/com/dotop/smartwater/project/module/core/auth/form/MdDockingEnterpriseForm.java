package com.dotop.smartwater.project.module.core.auth.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MdDockingEnterpriseForm extends BaseForm {
    private String id;
    private String enterpriseid;
    private String code;
    private String waterHost;
    private String factoryId;
    private Boolean status;
}