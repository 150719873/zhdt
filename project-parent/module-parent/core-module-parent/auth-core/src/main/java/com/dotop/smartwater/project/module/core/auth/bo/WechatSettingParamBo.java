package com.dotop.smartwater.project.module.core.auth.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @date 2018年7月6日 下午3:56:39
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatSettingParamBo extends BaseBo {

	private String enterprisename;

}
