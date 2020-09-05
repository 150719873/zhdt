package com.dotop.smartwater.project.module.core.auth.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**

 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PlatFormPerForm extends BaseForm {
    private String proleid;
    private List<String> permissionids;
}
