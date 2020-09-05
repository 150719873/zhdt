package com.dotop.smartwater.project.module.core.water.form;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信公共配置
 * 

 * @date 2019年4月1日
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatPublicSettingForm extends BaseForm {

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

	private String validtoken;

	private String enterpriseid;

	private String createuser;

	private String gatewayauthorizecode;

	private String gatewayopenidbycode;

	private String domain;

	private String wechatname;// 公众号名称

	private String servicephone;// 服务电话

	private String enterprisename;// 企业名称

	private String paybycardurl;

	private String qrcode;// 公众号二维码

}
