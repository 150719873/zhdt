package com.dotop.smartwater.dependence.core.common;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础类
 */
@Getter
@Setter
public class BaseDto {

	private String enterpriseid;

	private Integer offset;

	private Integer limit;

	private Date curr;

	private String userBy;

	private Integer isDel;

	private Integer newIsDel;

	private Boolean ifSort = Boolean.TRUE;
	
	private String sortName;
	
	private String sortValue;

}
