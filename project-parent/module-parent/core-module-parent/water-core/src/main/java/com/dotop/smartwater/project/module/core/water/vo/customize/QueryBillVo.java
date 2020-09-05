package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class QueryBillVo extends BaseVo {

	private String id;

	private String sumMonth;// 账单月份

	private Double sumRealpay;// 金额

	private Double sumWater;// 总用水量

	private Integer paystatus;// 缴费状态（0-未缴 1-已缴）

}
