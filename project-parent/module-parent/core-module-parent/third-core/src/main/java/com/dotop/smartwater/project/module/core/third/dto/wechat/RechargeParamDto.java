package com.dotop.smartwater.project.module.core.third.dto.wechat;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @date 2018年7月17日 上午10:50:20
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RechargeParamDto extends BaseDto {

	private String ownerid;// 充值的业主id

	private Double amount;// 充值金额
	
	private String userno;
	
	private String tradeno;// 账单流水tradeno

	private String wechatmchno;// 商户编号

	private String errormsg;// 微信返回的信息

	private Boolean payflag;// true支付成功，false支付失败

}
