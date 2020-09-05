package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 账单 个人收缴
 * 

 *
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class AccountingForm extends BaseForm {

	/** 账单核算ID */
	private String id;
	/** 平账ID */
	private String rid;
	/** 收银员ID */
	private String userid;
	/** 收银员账号 */
	private String account;
	/** 收银员姓名 */
	private String username;
	/** 系统核算金额 */
	private Double sys;
	/** 人工核算金额 */
	private Double artificial;
	/** 差值 */
	private Double differ;
	/** 补差 */
	private Double supplement;
	/** 核算日期（某天的核算日期，可能不在当天核算） */
	private String dtime;
	/** 日期（指具体某天） */
	private String atime;
	/** 核算状态（0-正常 1-异常 2-平账） */
	private Integer status;
	/** 提交状态（1-提交 0-未提交） */
	private Integer substatus;
	/** 备注 */
	private String remark;
	/** 创建时间 */
	private String createtime;
	/** 区域ID */
	private String communityid;

}
