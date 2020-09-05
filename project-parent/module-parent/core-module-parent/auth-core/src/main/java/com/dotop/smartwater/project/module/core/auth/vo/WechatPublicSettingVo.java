package com.dotop.smartwater.project.module.core.auth.vo;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @date 2018年7月6日 下午3:56:39
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatPublicSettingVo extends BaseVo {

	private String wechatpublicid;

	private Date createtime;

	private Date updatetime;

	private String appid;

	private String mchid;

	private String appsecret;

	private String paysecret;

	private String unifiedorderurl;

	private String orderqueryurl;

	private String requestreturnurl;

	private String paybycardurl;

	private String revokeorderurl;

	private String validtoken;

	private String enterpriseid;

	private String enterprisename;

	private String createuser;

	private String gatewayauthorizecode;

	private String gatewayopenidbycode;

	private String domain;

	private String wechatname;// 公众号名称

	private String servicephone;// 服务电话

	private String qrcode;// 公众号二维码

}
