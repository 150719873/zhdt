package com.dotop.pipe.core.bo.alarm;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmWMSettingBo extends BasePipeBo {
	private String id;
	private String deviceId;
	private String userId;
	private List<String> areaIds;
}
