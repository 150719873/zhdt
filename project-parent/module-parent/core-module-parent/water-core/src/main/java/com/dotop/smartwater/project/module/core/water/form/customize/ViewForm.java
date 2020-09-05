package com.dotop.smartwater.project.module.core.water.form.customize;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用于接收请求参数
 * 

 *
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class ViewForm extends BaseForm {
	// 模板ID
	private String tempid;
	// 查询字段名称
	private String name;
	// 查询字段值
	private String val;
	// 业主ID
	private String ownerid;
	// 操作员ID
	private String userid;

	// 支付类型 1-表示支付
	private Integer paytype;

}
