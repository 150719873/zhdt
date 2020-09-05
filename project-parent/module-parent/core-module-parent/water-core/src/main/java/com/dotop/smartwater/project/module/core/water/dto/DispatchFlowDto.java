package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程-主表
 * 

 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DispatchFlowDto extends BaseDto {
	private String id;
	/* 流程号 */
	private String flowno;
	/* 业务系统 1-营收 2-管漏 */
	private Integer sys;
	/* 业务系统文本 */
	private String systext;
	/* 类型 1-报装 2-报修 3-巡检 */
	private Integer type;
	/* 类型文本 */
	private String typetext;

	/* 标题 */
	private String title;
	/* 申请人ID */
	private String userid;
	/* 申请人 */
	private String username;
	/* 申请人账号/工号 */
	private String account;
	/* 申请时间 */
	private String atime;
	/* 审核人ID */
	private String auditorid;
	/* 审核人 */
	private String auditorname;
	/* 审核时间 */
	private String etime;
	/* 审核结果 1-true 0-false */
	private Integer result;
	/* 审核结果文本 */
	private String resulttext;
	/* 审核结果解析文本 */
	private String rtext;
	/* 审核状态 1、未查看 2、处理中 3、已完成 */
	private Integer status;
	/* 审核状态解析文本 */
	private String statustext;
	/* 流程状态 -1-已删除 0-已撤销 1-已申请 2-已挂起 3-处理中 4-已关闭 5-已归档 */
	private Integer flowstatus;
	/* 流程状态解析文本 */
	private String flowstatustext;
	/* 企业ID */
	// private Long enterpriseid;

	// 分页
	// private int pageCount;

	// private int page;

	/* 归档图片 */
	private String imgurl;
	/* 归档图片 */
	private String imgurl2;
	/* 归档图片 */
	private String imgurl3;
	/* 归档说明 */
	private String explain;

	private DispatchBillDto dispatchBill;
}
