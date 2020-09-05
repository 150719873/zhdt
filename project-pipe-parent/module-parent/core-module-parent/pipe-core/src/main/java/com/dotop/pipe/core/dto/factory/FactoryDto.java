package com.dotop.pipe.core.dto.factory;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @date 2018/10/25.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FactoryDto extends BasePipeDto {
	/**
	 * 厂商ID
	 */
	private String factoryId;

	/**
	 * 厂商编码
	 */
	private String code;

	/**
	 * 厂商名称
	 */
	private String name;

	/**
	 * 厂商描述
	 */
	private String des;
}
