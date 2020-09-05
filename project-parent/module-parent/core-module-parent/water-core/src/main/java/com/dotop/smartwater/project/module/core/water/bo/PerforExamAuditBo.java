package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 绩效考核-审核人员表
 * 

 * @date 2019年2月28日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PerforExamAuditBo extends BaseBo {

	/** 主键 */
	private String id;
	/** 批次号 */
	private String number;
	/** 考核人员ID */
	private String assessmentId;
	/** 考核人员名称 */
	private String assessmentName;
	/** 审核人员ID */
	private String auditId;
	/** 审核人员名称 */
	private String auditName;
	/** 审核时间 */
	private String auditTime;
	/** 审核状态 */
	private String auditStatus;
	/** 审核分数 */
	private String score;
	/** 是否及格 */
	private String isPass;
	/** 说明 */
	private String explain;

	/** 考核名称 */
	private String name;
	/** 结束时间 */
	private String endTime;
	/** 总分数 */
	private String totalScore;
	/** 及格分数 */
	private String passScore;
	/** 填报分数 */
	private String fillScore;
	/** 考核状态（0-未开始 1-处理中 2-已结束） */
	private String status;

}
