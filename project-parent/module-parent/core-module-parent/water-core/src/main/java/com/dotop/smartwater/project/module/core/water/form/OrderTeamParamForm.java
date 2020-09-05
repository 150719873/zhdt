package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2018/11/23.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderTeamParamForm extends BaseForm {
	/**
	 * 团队ID
	 */
	private String teamId;
	/**
	 * 团队名称
	 */
	private String teamName;
	/**
	 * 未完成工单数
	 */
	private Integer undoneOrderCount;
	/**
	 * 队长ID
	 */
	private String leaderId;
	/**
	 * 队长工号
	 */
	private String leaderWorkNum;
	/**
	 * 队长名称
	 */
	private String leaderName;
	/**
	 * 团队状态，1外勤，0空闲
	 */
	private Integer teamStatus;
	/**
	 * 队长电话
	 */
	private String leaderPhone;
	/**
	 * 企业ID
	 */
//	private Long enterpriseId;
	/**
	 * 成员ID
	 */
	private String[] memberIds;
}
