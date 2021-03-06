package com.dotop.smartwater.project.module.core.water.bo.customize;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class PreviewBo extends BaseBo {

	/* 抄表截止日期 */
	private String metertime;
	/* 账单月份 */
	private String month;
	/* 抄表截至日期间隔天数 超过此天数为异常 */
	private Integer intervalday;
	/* 生成方式 A-区域 B-水表号 */
	private String type;
	/* 区域ID */
	private String communityIds;
	/* 业主编号 */
	private String usernos;

	// 账单状态
	private String tradeStatus;

}
