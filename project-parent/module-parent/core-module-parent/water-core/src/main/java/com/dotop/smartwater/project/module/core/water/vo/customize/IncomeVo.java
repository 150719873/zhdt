package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class IncomeVo extends BaseVo {

	/* 已收 */
	private Double alreadyAmount;
	/* 未收 */
	private Double uncollectedAmount;

}
