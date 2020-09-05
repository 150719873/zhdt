package com.dotop.pipe.core.form;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @date 2018/10/22.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictionaryForm extends BasePipeForm {
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
	/**
	 * 0 启用 1 禁用
	 */
	private Integer isDel;
}
