package com.dotop.pipe.data.report.core.form;

import java.util.List;

import com.dotop.pipe.data.report.core.enums.TimingStatusEnum;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TimingCalculationForm extends BasePipeForm {

	private String tcId;

	private String code;

	private String name;

	private String des;

	private TimingStatusEnum status;

	private String upperLimit;

	private String lowerLimit;

	private List<TimingFormulaForm> formulas;

}
