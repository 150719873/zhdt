package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业主开户/换表

 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerChangeDto extends BaseDto{

	/**业主ID*/
	private String ownerid;
	/**新设备ID*/
	private String devid;
	/**旧设备ID*/
	private String oldDevid;
}
