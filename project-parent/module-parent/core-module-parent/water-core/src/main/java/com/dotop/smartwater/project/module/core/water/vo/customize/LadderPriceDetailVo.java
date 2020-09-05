package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同pay_type_ladder
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class LadderPriceDetailVo extends BaseVo {
	private String name;
	private Double amount;

}
