package com.dotop.smartwater.project.module.core.water.bo;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 绩效考核-考核人员/对象表
 * 

 * @date 2019年2月28日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PerforExamPersonnelBo extends BaseBo {

	/** 主键 */
	private String id;
	/** 批次号 */
	private String number;
	/** 姓名 */
	private String name;
	/** 考核对象ID */
	private String assessmentId;
	/** 考核对象名称 */
	private String assessmentName;
	/** 提交时间 */
	private String submitDate;
	/** 已审核人数 */
	private String auditeds;
	/** 填报分数 */
	private String fillScore;
	/** 最终得分 */
	private String finalScore;
	/** 是否及格 */
	private String isPass;
	/** 提交状态（ 0-未提交 1-已提交 2-已过期） */
	private String submitStatus;
	/** 审核状态（0-未审核 1-审核中 2-已提交 3-已过期） */
	private String auditStatus;
	/** 填报详情 */
	private List<PerforExamFillBo> fillArry;
	/** 考核状态（0-未开始 1-处理中 2-已结束） */
	private String status;
}
