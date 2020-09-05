package com.dotop.smartwater.project.third.module.core.third.zhsw.form;


import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class MeterRecordForm extends BaseForm {

    private String id;

    private String wcode;

    private int current_time;

    private BigDecimal current_num;
}
