package com.dotop.smartwater.project.module.core.water.form.customize;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: project-parent
 * @description:

 * @create: 2019-04-15 19:24
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class BalanceChangeParamForm extends BaseForm {
    private String ownerid;

    private String tradeNo;
}
