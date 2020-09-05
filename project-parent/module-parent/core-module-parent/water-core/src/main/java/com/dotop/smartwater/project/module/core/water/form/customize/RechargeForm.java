package com.dotop.smartwater.project.module.core.water.form.customize;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 充值-用于充值时生成代金券的接收参数
 * 

 *
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class RechargeForm extends BaseForm {

	private String userno;

	private Double amount;

	private String ownerid;

	private String wechatmchno;// 商户编号

	private String errormsg;// 微信返回的信息
	
	private String tradeno;// 账单流水tradeno

	private Boolean payflag;// true支付成功，false支付失败

}
