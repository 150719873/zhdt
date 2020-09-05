package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class DictionaryForm extends BaseForm {
    private String dictionaryId;

    private String dictionaryCode;

    private String dictionaryType;

    private String dictionaryName;

    private String dictionaryValue;

    private String remark;

    private List<String> dictionaryIds;
}