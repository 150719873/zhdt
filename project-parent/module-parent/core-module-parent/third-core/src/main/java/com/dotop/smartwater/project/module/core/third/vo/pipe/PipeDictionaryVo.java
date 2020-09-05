package com.dotop.smartwater.project.module.core.third.vo.pipe;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PipeDictionaryVo {

	private String id;

	private String name;

	private String val;

	private String des;
	// 类型
	private String type;
	// 单位
	private String unit;

}
