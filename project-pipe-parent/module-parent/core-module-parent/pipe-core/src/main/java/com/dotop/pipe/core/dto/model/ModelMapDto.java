package com.dotop.pipe.core.dto.model;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ModelMapDto extends BasePipeDto {

	// 主键
	private String mapId;

	// 模型id
	private String modelId;

	// 产品主键
	private String productId;

	// 设备主键
	private String deviceId;

}