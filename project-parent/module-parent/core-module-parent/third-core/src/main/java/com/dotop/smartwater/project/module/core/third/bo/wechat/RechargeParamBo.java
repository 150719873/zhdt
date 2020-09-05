package com.dotop.smartwater.project.module.core.third.bo.wechat;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @date 2018年7月17日 上午10:50:20
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RechargeParamBo extends BaseBo {

	private String userno;

	private Double amount;

	private String ownerid;

	private String wechatmchno;// 商户编号

	private String tradeno;// 账单流水tradeno
	
	private String errormsg;// 微信返回的信息

	private Boolean payflag;// true支付成功，false支付失败
	
	

}
