package com.dotop.smartwater.project.module.core.water.form.customize;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 小区欠费自动关阀配置保存
 * 

 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class AutoCloseConfigForm extends BaseForm {

	private String communityId;

	private Float config;

}