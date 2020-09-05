package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DictionaryChildForm extends BaseForm {
    private String childId;

    private String dictionaryId;

    private String childName;

    private String childValue;

    private String remark;
    
}