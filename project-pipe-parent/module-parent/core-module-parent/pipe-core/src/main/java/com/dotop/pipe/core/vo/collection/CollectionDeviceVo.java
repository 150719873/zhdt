package com.dotop.pipe.core.vo.collection;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CollectionDeviceVo extends BasePipeVo {

	private String id;
	private String deviceId;
	private String userId;
	private String code;
	private String des;
	private String name;
	private String type;
}
