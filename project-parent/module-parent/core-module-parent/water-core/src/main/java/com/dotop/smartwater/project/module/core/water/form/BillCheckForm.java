package com.dotop.smartwater.project.module.core.water.form;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BillCheckForm extends BaseForm {

	/**
	 * 账单对账
	 */
	private String billCheckId;

	/**
	 * 流水号
	 */
	private String serialNumber;

	/**
	 * 对账标题
	 */
	private String billTitle;

	/**
	 * 账单状态
	 */
	private String billStatus;

	/**
	 * 流程状态
	 */
	private String processStatus;

	/**
	 * 流程id
	 */
	private String processId;

	/**
	 * 对账开始时间
	 */
	private Date startDate;

	/**
	 * 对账结束时间
	 */
	private Date endDate;
	
	/**
	 * 流程申请人
	 */
	private String processCreateBy;

}
