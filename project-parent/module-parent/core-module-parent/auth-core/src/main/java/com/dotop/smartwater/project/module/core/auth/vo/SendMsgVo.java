package com.dotop.smartwater.project.module.core.auth.vo;

import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SendMsgVo extends BaseVo {
//	private String enterpriseid;
	private Integer modeltype;
	private String[] phoneNumbers;
	private Map<String, String> params;

	// 微信消息需要的字段
	private String openId;
	private WechatMessageParamVo wechatMessageParamVo;
}
