package com.dotop.smartwater.project.module.core.auth.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserParamForm extends BaseForm {
	private String userid;
	private String ticket;
	private String enterpriseid;
	private String account;
	private String name;
	private Integer type;
	private String roleid;
}
