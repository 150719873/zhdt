package com.dotop.pipe.core.form;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmSettingProperties extends BasePipeForm {

	private String tag;
	private String field;
	private String maxValue;
	private String minValue;
	private String maxCompare;
	private String minCompare;
	private String des;
}
