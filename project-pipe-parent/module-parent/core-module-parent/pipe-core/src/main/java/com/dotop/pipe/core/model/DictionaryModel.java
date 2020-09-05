package com.dotop.pipe.core.model;

import com.dotop.smartwater.dependence.core.common.RootModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DictionaryModel extends RootModel {

	// 主键
	private String id;

	// 名字
	private String name;

	// 值
	private String val;

	// 类型
	private String type;

	// 单位
	private String unit;

	// 描述
	private String des;

}
