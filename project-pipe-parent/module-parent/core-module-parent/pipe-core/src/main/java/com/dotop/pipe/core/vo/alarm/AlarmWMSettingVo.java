package com.dotop.pipe.core.vo.alarm;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AlarmWMSettingVo extends BasePipeVo {

	private String name;
	private String unit;
	private String field;
	private String val;
	private AlarmSettingVo alarmSettingVo;
	private Boolean status = false;

	AlarmWMSettingVo() {

	}

	public AlarmWMSettingVo(String name, String field, String val, String unit, AlarmSettingVo alarmSettingVo) {
		this.name = name;
		this.field = field;
		this.val = val;
		this.unit = unit;
		this.alarmSettingVo = alarmSettingVo;
	}

}
