package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019年3月4日
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterTmplDto extends BaseDto {

	private String id;
	private String code;
	private String name;
	private String desc;

	private WorkCenterFormDto form;

	// 是否有效
	private String ifEffect;

}
