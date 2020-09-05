package com.dotop.smartwater.project.module.core.water.vo;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2018/11/22.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderTeamVo extends BaseVo {
	/**
	 * 团队ID
	 */
	private String id;
	/**
	 * 团队名称
	 */
	private String teamName;
	/**
	 * 团队状态，0空闲，1外勤
	 */
	private String teamStatus;
	/**
	 * 未完成工单数
	 */
	private Integer undoneOrderCount;
	/**
	 * 队长ID
	 */
	private String leaderId;
	/**
	 * 队长名称
	 */
	private String leaderName;
	/**
	 * 队长工号
	 */
	private String leaderWorkNum;
	/**
	 * 队长电话
	 */
	private String leaderPhone;

	/**
	 * 成员
	 */
	private List<OrderTeamMembersVo> members;

	/**
	 * 企业ID
	 */
	// private Long enterpriseid;
}
