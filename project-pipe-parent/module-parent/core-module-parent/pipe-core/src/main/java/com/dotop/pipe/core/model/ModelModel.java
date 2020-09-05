package com.dotop.pipe.core.model;

import com.dotop.smartwater.dependence.core.common.RootModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

//模型
@Data
@EqualsAndHashCode(callSuper = true)
public class ModelModel extends RootModel {

	// 主键
	private String modelId;

	// 产品主键
	private String productId;

	// 版本
	private String version;

	// 内容
	private String content;

}
