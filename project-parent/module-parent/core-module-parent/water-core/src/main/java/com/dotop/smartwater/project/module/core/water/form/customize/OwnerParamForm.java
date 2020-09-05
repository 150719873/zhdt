package com.dotop.smartwater.project.module.core.water.form.customize;

import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerParamForm extends OwnerForm {

	private String startYear;

	private String startMonth;

	private String endMonth;

	private String endYear;

}