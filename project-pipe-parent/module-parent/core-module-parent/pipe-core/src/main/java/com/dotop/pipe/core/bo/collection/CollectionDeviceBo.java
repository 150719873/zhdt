package com.dotop.pipe.core.bo.collection;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CollectionDeviceBo extends BasePipeBo {
	private String id;
	private String deviceId;
	private String userId;
	private String type;
}
