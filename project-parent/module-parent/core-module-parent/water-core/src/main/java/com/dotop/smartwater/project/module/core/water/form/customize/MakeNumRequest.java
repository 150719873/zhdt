package com.dotop.smartwater.project.module.core.water.form.customize;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MakeNumRequest extends BaseForm {
	private Integer count;
	private Integer ruleid;

	public MakeNumRequest() {

	}

	public MakeNumRequest(Integer count, Integer ruleid) {
		this.count = count;
		this.ruleid = ruleid;
	}

}
