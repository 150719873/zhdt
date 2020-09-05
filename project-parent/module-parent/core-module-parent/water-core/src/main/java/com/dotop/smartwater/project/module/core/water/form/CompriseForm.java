package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 费用组成
 * 

 *
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = true)
public class CompriseForm extends BaseForm {

	private String id;

	private String typeid;

	private String name;

	private String createtime;

}
