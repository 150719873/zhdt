package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 活动规则
 * 

 *
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class ConditionBo extends BaseBo {
	/* ID */
	private String id;
	/* 活动ID */
	private String discountid;
	/* 编号 */
	private String no;
	/* 充值金额 */
	private Double amount;
	/* 方式 */
	private Integer mode;
	/* 面值 */
	private Double value;
	/* 单位 */
	private String unit;
	/* 创建时间 */
	private String createtime;

}
