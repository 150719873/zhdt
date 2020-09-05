package com.dotop.smartwater.project.module.core.third.form.iot;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserEntryForm extends BaseForm {

	private String account;
	private String password;

}
