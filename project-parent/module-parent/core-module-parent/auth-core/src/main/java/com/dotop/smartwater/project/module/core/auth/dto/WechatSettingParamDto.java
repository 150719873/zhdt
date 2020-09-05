package com.dotop.smartwater.project.module.core.auth.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @date 2018年7月6日 下午3:56:39
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatSettingParamDto extends BaseDto {

	private String enterprisename;

}
