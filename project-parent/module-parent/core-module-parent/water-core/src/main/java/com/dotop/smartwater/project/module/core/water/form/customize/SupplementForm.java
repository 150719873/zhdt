package com.dotop.smartwater.project.module.core.water.form.customize;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 补差
 * 

 *
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class SupplementForm extends BaseForm {

	/** 核算ID */
	private String aid;

	/** 年 */
	private String year;
	/** 月 */
	private String month;
	/** 收营员ID */
	private String userid;
	/** 收营员账号 */
	private String account;
	/** 补差 */
	private Double supplement;

	private String atime;

	private String username;

	private Double sys;

	private Double artificial;

	private Double differ;

	private String explain;

	private String remark;

	private Integer status;

	private String operauserid;

	private String operausername;

	private String operatime;

	private String createtime;

}
