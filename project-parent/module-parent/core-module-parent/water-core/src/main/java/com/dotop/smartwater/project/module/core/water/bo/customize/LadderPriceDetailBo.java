package com.dotop.smartwater.project.module.core.water.bo.customize;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同pay_type_ladder
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class LadderPriceDetailBo extends BaseBo {
	private String name;
	private Double amount;

}
