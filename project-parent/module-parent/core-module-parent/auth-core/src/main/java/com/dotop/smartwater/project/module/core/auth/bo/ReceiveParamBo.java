package com.dotop.smartwater.project.module.core.auth.bo;

import lombok.Data;

/**

 * @date 2018/12/14.
 */
@Data
public class ReceiveParamBo {
	/**
	 * 消息类型，1短信，2邮件,3微信
	 */
	private Integer messagetype;
	/**
	 * 接收人名称
	 */
	private String receiveusername;
	/**
	 * 接收地址,发送短信时添加电话号码，发送邮件时填写邮箱地址
	 */
	private String receiveaddress;

}
