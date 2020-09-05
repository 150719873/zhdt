package com.dotop.smartwater.project.module.core.third.bo.wechat;

import lombok.Data;

/**
 * 消息基类
 * 
 */

@Data
public class BaseMessage {
	// 接收方帐号（收到的OpenID）
	protected String ToUserName;
	// 开发者微信号
	protected String FromUserName;
	// 消息创建时间 （整型）
	protected long CreateTime;
	// 消息类型（text/music/news）
	protected String MsgType;
	// 位0x0001被标志时，星标刚收到的消息
	protected int FuncFlag;
	/** 事件类型，subscribe(订阅)、unsubscribe(取消订阅) **/
	protected String Event;

}