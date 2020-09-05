package com.dotop.pipe.core.model;

import com.dotop.smartwater.dependence.core.common.RootModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @date 2018/10/25.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductModel extends RootModel {

	/**
	 * 产品ID
	 */
	private String productId;

	/**
	 * 厂商ID
	 */
	private String factoryId;

	/**
	 * 产品类别
	 */
	private String category;

	/**
	 * 产品类型
	 */
	private String type;

	/**
	 * 产品编码
	 */
	private String code;

	/**
	 * 产品名称
	 */
	private String name;

	/**
	 * 产品描述
	 */
	private String des;

	/**
	 * 口径
	 */
	private String caliber;

	/**
	 * 材质
	 */
	private String material;

	/**
	 * 版本
	 */
	private String version;
}
