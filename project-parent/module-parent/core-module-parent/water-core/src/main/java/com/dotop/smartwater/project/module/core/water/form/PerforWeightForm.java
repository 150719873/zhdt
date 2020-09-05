package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @date 2019年2月26日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PerforWeightForm extends BaseForm {

	/** 主键 */
	private String id;
	/** 标题 */
	private String title;
	/** 描述 */
	private String describe;
	/** 分数 */
	private String score;

}
