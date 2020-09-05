package com.dotop.pipe.core.dto.point;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PointMapDto extends BasePipeDto {

	// 主键
	private String mapId;

	// 坐标主键
	private String pointId;

	// 设备主键
	private String deviceId;

	// 坐标主键
	private List<String> pointIds;

	// 设备主键
	private List<String> deviceIds;

}
