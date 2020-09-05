package com.dotop.smartwater.project.module.core.auth.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**

 * @date 2019/8/8.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleAreaForm extends BaseForm {
	public String roleId;

	public List<String> areaIds;
}
