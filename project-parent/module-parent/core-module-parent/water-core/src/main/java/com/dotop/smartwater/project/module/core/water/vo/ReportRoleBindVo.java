package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReportRoleBindVo extends BaseVo {

	private String id;

	private String roleid;

	private String bindid;

	// 冗余bindid返回对象
	private ReportBindVo reportBind;
}
