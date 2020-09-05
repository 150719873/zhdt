package com.dotop.smartwater.project.module.core.auth.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**

 * @date 2019/8/7.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DataPermissionPerForm extends BaseForm{
	private String id;
	private List<String> permissionids;
}
