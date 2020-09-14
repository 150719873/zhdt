package com.dotop.smartwater.project.third.concentrator.core.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BookForm extends BaseForm {

    /**
     * 集中器
     * join id
     */
    private ConcentratorForm concentrator;

    /**
     * 采集器
     * join id
     */
    private CollectorForm collector;
}
