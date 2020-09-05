package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标记账单异常
 * 
 同order_mark
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class MarkOrderBo extends BaseBo {

	private String id;

	/** 账单流水号 */
	private String tradeno;
	/** 标记异常说明 */
	private String remark;
	/** 状态 1-异常 2-撤销 */
	private Integer status;
	/** 标记时间 */
	private String marktime;
	/** 标记人 */
	private String userid;
	/** 标记人姓名 */
	private String username;
	/** 创建时间 */
	private String createtime;

}
