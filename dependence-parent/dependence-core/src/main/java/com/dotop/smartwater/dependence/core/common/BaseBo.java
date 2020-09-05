package com.dotop.smartwater.dependence.core.common;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础类
 */
@Getter
@Setter
public class BaseBo {

	private String enterpriseid;

	private Integer page;

	private Integer pageCount;

	private Integer limit;

	private Date curr;

	private String userBy;

	private Integer isDel;

	private Integer newIsDel;
	
	private String sortName;
	
	private String sortValue;
}
