package com.dotop.smartwater.project.module.core.water.form.customize;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同pay_type_ladder
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class LadderPriceDetailForm extends BaseForm {
	private String name;
	private Double amount;

}
