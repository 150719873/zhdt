package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SmsConfigBo extends BaseBo {

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

}
