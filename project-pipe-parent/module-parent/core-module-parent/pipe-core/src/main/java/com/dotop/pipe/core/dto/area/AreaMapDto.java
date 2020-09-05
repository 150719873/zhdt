package com.dotop.pipe.core.dto.area;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Deprecated
@Data
@EqualsAndHashCode(callSuper = true)
public class AreaMapDto extends BasePipeDto {

	// 主键
	private String mapId;

	// 区域主键
	private String areaId;

	// 设备主键
	private String deviceId;

}
