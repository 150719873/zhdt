package com.dotop.smartwater.project.module.core.water.form.customize;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @program: project-parent
 * @description: 账单催缴

 * @create: 2019-04-02 17:21
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class RemindersForm extends BaseForm {
    private Integer modelId;
    private String days;
    private String batchNo;
    private List<String> communityIds;
}
