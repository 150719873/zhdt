package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @description 通讯方式配置Form
 * @date 2019年10月17日 15:45
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ModeBindForm extends BaseForm {
	// 主键
	private String id;
	// 通讯方式（字典ID）
	private String mode;
	// 通讯方式名称
	private String modeName;
}
