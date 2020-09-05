package com.dotop.pipe.core.form;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmWMSettingForm extends BasePipeForm {
	private String id;
	private String deviceId;
	private List<String> areaIds;
}
