package com.dotop.pipe.core.form;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmSettingForm extends BasePipeForm {

	private String id;
	private String deviceCode;
	private String deviceId;
	private List<String> deviceIds;
	private String tag;
	private String field;
	private String maxValue;
	private String minValue;
	private String maxCompare;
	private String minCompare;
	private List<AlarmSettingProperties> properties;
}
