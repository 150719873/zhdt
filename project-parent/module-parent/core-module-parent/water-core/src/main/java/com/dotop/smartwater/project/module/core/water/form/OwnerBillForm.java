package com.dotop.smartwater.project.module.core.water.form;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同owner_bill
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerBillForm extends BaseForm {
	private String id;

	private String ownerid;

	private Double realwater;

	private Double realpay;

	private String createuser;

	private Date createtime;

}