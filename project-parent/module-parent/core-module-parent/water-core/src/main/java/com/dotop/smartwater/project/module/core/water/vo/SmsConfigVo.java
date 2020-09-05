package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019年2月23日
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SmsConfigVo extends BaseVo {

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
	
	private String enterprisename;

}
