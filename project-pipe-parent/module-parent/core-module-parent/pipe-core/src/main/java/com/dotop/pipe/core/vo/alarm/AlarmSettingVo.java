package com.dotop.pipe.core.vo.alarm;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AlarmSettingVo extends BasePipeVo {

	private String id;
	private String deviceId;
	private List<String> deviceIds;
	private String tag;
	private String field;
	private String maxValue;
	private String minValue;
	private String maxCompare;
	private String minCompare;
	// 当前流量值
	private String value;
	private String des;

}
