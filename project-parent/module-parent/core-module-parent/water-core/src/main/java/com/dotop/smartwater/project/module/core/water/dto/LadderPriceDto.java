package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收费区间-费用组成单价
 * 

 *
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class LadderPriceDto extends BaseDto {

	private String id;
	/* 收费类型ID */
	private String typeid;
	/* 收费区间ID */
	private String ladderid;
	/* 费用组成名称 */
	private String name;
	/* 费用组成ID */
	/*private String compriseid;*/
	/* 收费区间中费用组成单价 */
	private Double price;
	/* 创建时间 */
	private String createtime;

}
