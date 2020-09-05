package com.dotop.pipe.core.form;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @date 2018/10/25.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductForm extends BasePipeForm {

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
	 * 下拉过滤选择类型
	 */
	private String selectType;

}
