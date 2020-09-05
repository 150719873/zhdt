package com.dotop.smartwater.project.module.core.third.dto.wechat;

import java.util.Date;

import lombok.Data;

/**
 * 

 * @date 2018年7月17日 上午11:20:08
 * @version 1.0.0
 */
@Data
public class WechatOrderDto {

	private String id;//
	private Date createtime;// 创建时间
	private Date updatetime;// 更新时间
	private String ownerid;// 业主id
	private String orderid;// 订单id
	private String wechatmchno;// 微信商户单号
	private Integer wechatorderstatus;// 微信支付当前状态
	private Date wechatpaytime;// 微信支付时间
	private String wechatpaytype;// 微信支付方式：JSAPI 公众号支付;NATIVE 扫码支付;APP APP支付
	private Double wechatamount;// 微信支付金额
	private String prepayid;// 预支付交易会话标识
	private String wechatresultcode;// 微信返回状态code值
	private String wechatretruncode;// 微信数据返回状态
	private String wechatsign;// 微信标识信息
	private String wechaterrormsg;// 微信错误返回的信息
	private String orderpayparam;// 微信支付请求参数
	private Integer wechatorderstate;// 微信订单是否正常状态，0-正常，1-异常，默认正常
	private Integer paytype;// 1-缴费，2-充值，默认缴费
	private String remark;// 备注信息
	private String attach;// 微信附加数据
	private String tradeno;// 订单号

}
