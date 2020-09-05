package com.dotop.smartwater.project.module.core.demo.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HelloForm extends BaseForm {

	private String name;
}
