package com.dotop.smartwater.project.module.core.third.bo.wechat;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文本消息
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TextMessage extends BaseMessage {
	// 回复的消息内容
	private String Content;

}