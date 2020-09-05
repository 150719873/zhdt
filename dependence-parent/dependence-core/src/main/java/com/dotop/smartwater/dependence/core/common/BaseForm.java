package com.dotop.smartwater.dependence.core.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础类
 */
@Getter
@Setter
public class BaseForm {

	private String enterpriseid;

	private Integer page;

	private Integer pageCount;

	private Integer limit;
	
	private String sortName;
	
	private String sortValue;

}
