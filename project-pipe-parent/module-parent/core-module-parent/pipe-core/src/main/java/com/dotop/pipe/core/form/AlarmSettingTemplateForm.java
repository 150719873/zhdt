package com.dotop.pipe.core.form;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmSettingTemplateForm extends BasePipeForm {
    private String id;
    private String code;
    private String name;
    private String content;
    private String productCategory;
    private String productType;
}
