package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业主开户/换表

 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerChangeForm extends BaseForm{
	
	/**业主ID*/
	private String ownerid;
	/**新设备ID*/
	private String devid;
	/**旧设备ID*/
	private String oldDevid;
	
}
