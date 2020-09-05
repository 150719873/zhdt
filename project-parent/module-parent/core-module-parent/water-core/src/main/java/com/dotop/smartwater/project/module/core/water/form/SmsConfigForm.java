package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信绑定
 * 

 * @date 2019年2月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SmsConfigForm extends BaseForm {

	/**
	 * 配置id
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;

	private String code;

	/**
	 * 签名
	 */
	private String sign;

	private String mkey;

	private String mkeysecret;

	/**
	 * 状态 0 启用 1 禁用
	 */
	private Integer status;

	/**
	 * 备注
	 */
	private String remarks;

	/**
	 * 0:正常,1:删除
	 */
	private String isDel;
}
