package com.dotop.smartwater.project.module.core.water.dto;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

//表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class ReportBindDto extends BaseDto {

	// 主键
	private List<String> bindids;

	// 主键
	private String bindid;

	// 企业id
	private String enterpriseid;

	// 报表资源id
	private String reportid;
	// 报表资源名字
	private String reportname;

	// 描述
	private String description;

	// 类型 快逸,quiee/帆软,fine/润乾,raqsoft
	private String type;

	// 创建时间
	private Date createtime;
	// 创建人
	private String createuser;

	// 查询冗余字段
	// 企业名字
	private String enterprisename;

}
