package com.dotop.smartwater.project.module.core.auth.bo;

import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SendMsgBo extends BaseBo {
//	private String enterpriseid;
	private Integer modeltype;
	private String[] phoneNumbers;
	private Map<String, String> params;

	// 微信消息需要的字段
	private String openId;
	private WechatMessageParamBo wechatMessageParam;

}
