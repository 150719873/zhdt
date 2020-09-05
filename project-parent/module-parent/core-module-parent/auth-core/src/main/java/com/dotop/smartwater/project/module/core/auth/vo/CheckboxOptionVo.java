package com.dotop.smartwater.project.module.core.auth.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019/8/12.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CheckboxOptionVo extends BaseVo{

	/**
	 * 名称
	 */
	private String label;

	/**
	 * 值
	 */
	private String value;

	/**
	 * 是否选中
	 */
	private boolean checked;
}
