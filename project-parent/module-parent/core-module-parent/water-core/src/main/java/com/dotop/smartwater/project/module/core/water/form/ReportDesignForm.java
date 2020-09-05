package com.dotop.smartwater.project.module.core.water.form;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * 报表展示设计Form

 * @date 2019-07-22
 *
 */

//表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class ReportDesignForm extends BaseForm {
	
	//主键
	private String reportDesignId;
	//名称
	private String name;
	//说明
	private String introduction;
	//创建人ID
	private String createUserId;
	//创建人姓名
	private String createUsername;
	//创建时间
	private Date createTime;
	//报表展示设计报表序列list
	private List<ReportRelationForm> reportRelations;
}

