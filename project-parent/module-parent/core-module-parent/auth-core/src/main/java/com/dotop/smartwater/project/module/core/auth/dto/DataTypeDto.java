package com.dotop.smartwater.project.module.core.auth.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019/8/7.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DataTypeDto extends BaseDto{

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 数据类型名称
	 */
	private String name;
}
