package com.dotop.smartwater.project.module.core.auth.form;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserAreaForm extends BaseForm {
	public String userid;

	public List<String> areaids;
}
