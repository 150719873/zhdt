package com.dotop.smartwater.project.module.core.auth.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AreaBo extends BaseBo {

	private String id;

	private String pId;

	private String name;
	//区域编号
	private String code;
}
