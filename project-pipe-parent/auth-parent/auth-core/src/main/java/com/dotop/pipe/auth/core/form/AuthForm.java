package com.dotop.pipe.auth.core.form;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthForm extends BasePipeForm {

	private String cas;

	// private String userId;

	// private String ticket;

	private String modelId;
}
