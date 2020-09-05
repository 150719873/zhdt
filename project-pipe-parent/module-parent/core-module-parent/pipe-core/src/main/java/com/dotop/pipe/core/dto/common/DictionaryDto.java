package com.dotop.pipe.core.dto.common;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @date 2018/10/22.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictionaryDto extends BasePipeDto {
	private String id;
	private String name;
	private String val;
	private String des;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 单位
	 */
	private String unit;

}
