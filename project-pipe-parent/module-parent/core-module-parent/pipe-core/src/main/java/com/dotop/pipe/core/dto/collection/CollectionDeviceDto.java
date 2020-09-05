package com.dotop.pipe.core.dto.collection;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CollectionDeviceDto extends BasePipeDto {
	private String id;
	private String deviceId;
	private String userId;
	private String type;
}
