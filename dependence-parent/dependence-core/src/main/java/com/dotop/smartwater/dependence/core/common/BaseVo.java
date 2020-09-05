package com.dotop.smartwater.dependence.core.common;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础类
 */
@Getter
@Setter
public class BaseVo {

	private String enterpriseid;

	// 创建人
	private String createBy;

	// 创建时间
	private Date createDate;

	// 最后修改人
	private String lastBy;

	// 最后修改时间
	private Date lastDate;

	// 是否删除
	private Integer isDel;
}
