package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 水表用途
 * 
 同purpose
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class PurposeForm extends BaseForm {

	private String id;

	private String enterpriseid;

	private String name;

	private Integer enable;

	private String remark;

	private String createuser;

	private String username;

	private String createtime;

	private String account;

}
