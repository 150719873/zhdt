package com.dotop.pipe.core.dto.product;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @date 2018/10/25.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductDto extends BasePipeDto {
	/**
	 * 产品ID
	 */
	private String productId;
	/**
	 * 产品类别
	 */
	private String category;
	/**
	 * 产品编码
	 */
	private String code;
	/**
	 * 产品名称
	 */
	private String name;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 产品描述
	 */
	private String des;

	/**
	 * 产品编码(模糊搜索)
	 */
	private String code_;
	/**
	 * 产品名称(模糊搜索)
	 */
	private String name_;
}
