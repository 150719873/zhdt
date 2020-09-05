package com.dotop.pipe.core.dto.alarm;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmWMSettingDto extends BasePipeDto {
	private String id;
	private String deviceId;
	private String userId;
	private List<String> areaIds;
	// 产品id集合
    private List<String> productIds;

}
