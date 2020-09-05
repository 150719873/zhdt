package com.dotop.smartwater.project.module.core.water.dto;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同systemlog 记录非法访问请求
 */
// 存在表
@Data
@EqualsAndHashCode(callSuper = false)
public class LogBeanDto extends BaseDto {

	private String id;
	private String requestMod;
	private String userid;
	private String ipaddress;
	private String requestParams;
	private Date requestTime;
	private Long cost;

}