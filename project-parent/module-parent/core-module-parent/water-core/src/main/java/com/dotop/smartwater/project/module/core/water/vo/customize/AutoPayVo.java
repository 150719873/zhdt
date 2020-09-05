package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 处理自动扣费的账单
 * 
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class AutoPayVo extends OrderVo {

	private String cardid;
	private String purposeinfo;
	private Double alreadypay;
	private Integer ischargebacks;

}
