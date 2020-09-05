package com.dotop.pipe.data.report.core.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TimingFormulaForm extends BaseForm {

	private String tfId;

	private String deviceId;

	private String direction;

	private String multiple;

	private String tcId;

}
