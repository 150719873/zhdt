package com.dotop.pipe.core.dto.alarm;

import java.util.List;

import com.dotop.pipe.core.form.AlarmSettingProperties;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmSettingDto extends BasePipeDto {
	private String id;
	private String deviceId;
	private String deviceCode;
	private List<String> deviceIds;
	private String tag;
	private String field;
	private String maxValue;
	private String minValue;
	private String maxCompare;
	private String minCompare;
	private List<AlarmSettingProperties> properties;
}
