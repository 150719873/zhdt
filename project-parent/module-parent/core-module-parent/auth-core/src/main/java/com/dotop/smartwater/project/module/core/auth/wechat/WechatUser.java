package com.dotop.smartwater.project.module.core.auth.wechat;

import lombok.Data;

@Data
public class WechatUser {

	public WechatUser(String ownerid, String openid, String enterpriseid, String token) {
		this.openid = openid;
		this.ownerid = ownerid;
		this.enterpriseid = enterpriseid;
		this.token = token;
	}

	// 业主id
	private String ownerid;
	// 绑定微信id
	private String openid;
	// 企业id
	private String enterpriseid;

	private String token;

}
