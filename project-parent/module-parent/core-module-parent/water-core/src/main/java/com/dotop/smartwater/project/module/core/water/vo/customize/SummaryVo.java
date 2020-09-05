package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

//表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class SummaryVo extends BaseVo {

	/** 操作员ID */
	private String userid;
	/** 操作员账号 */
	private String account;
	/** 操作员姓名 */
	private String username;
	/** 系统核算金额 */
	private Double sys;
	/** 人工核算金额 */
	private Double artificial;
	/** 差值 */
	private Double differ;
	/** 状态 0-正常 1-异常 */
	private Integer status;
	/** 核算时间范围 */
	private String rangetime;
	/** 年 */
	private String year;
	/** 月 */
	private String month;
	/** 日 */
	private String day;
}
