package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @date 2019年2月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SmsConfigDto extends BaseDto {

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
	private Integer isDel;

}
