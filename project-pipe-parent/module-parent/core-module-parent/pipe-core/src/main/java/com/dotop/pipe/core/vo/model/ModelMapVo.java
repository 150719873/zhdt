package com.dotop.pipe.core.vo.model;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ModelMapVo extends BasePipeVo {

	private String deviceId;

	private String productId;

	private String content;
}
