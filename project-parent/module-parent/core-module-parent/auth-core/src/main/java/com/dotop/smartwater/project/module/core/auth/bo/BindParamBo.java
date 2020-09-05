package com.dotop.smartwater.project.module.core.auth.bo;

import lombok.Data;

/**
 * 绑定参数
 * 

 *
 */
@Data
public class BindParamBo {

	/* 企业ID */
	private String enterpriseid;
	/* 模板ID */
	private String designid;
	/* 模板类型 */
	private Integer smstype;

	private String smstypename;

	private Integer isprint;

}
