package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class JSApiTicketVo extends BaseVo {

	private String timestamp;

	private String nonceStr;

	private String signature;

	private String appId;

}
