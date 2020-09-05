package com.dotop.smartwater.project.module.core.water.form;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 收费
 * 
 同pay_type
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class PayTypeForm extends BaseForm {

	/* 收费种类ID */
	private String typeid;
	/* 收费名称 */
	private String name;
	/* 预期天数 */
	private int overdueday;
	/* 保底收费 */
	private Double guarantees;
	/* 备注 */
	private String remark;
	/* 创建人ID */
	private String createuser;
	/* 创建人姓名 */
	private String username;
	/* 创建人姓名 */
	private String account;
	/* 创建时间 */
	private String createtime;
	/* 收费种类 */
	private List<CompriseForm> comprises;
	/* 收费区间 */
	private List<LadderForm> ladders;
	
	/* 发票类型(1-普通发票 2-增值税发票） */
	private String invoiceType;
	/* 是否启用滞纳金（1-启动 0-停用） */
	private String isLatefee;
	/* 按日加收（1-是 0-否） */
	private String increase;
	/* 滞纳金比率 */
	private String ratio;

	private Boolean taskSwitch;
	private String taskMonth;
	private String taskDay;
	private Integer taskIntervalDay;
	private Date updated;
}
