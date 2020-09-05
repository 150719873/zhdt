package com.dotop.smartwater.project.module.core.water.dto.customize;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WechatParamDto extends BaseDto {

	private String ownerid;

	private String tradeno;

	private String orderid;

	private String username;

	private Integer isdefaultblind;

	private String usermsg;// 用户编号， 业主电话，身份证

	private Integer ischargebacks;

	private String orderids;

	// 商户编号
	private String wechatmchno;

	private String errormsg;

	// 充值需要字段
	// session
	private String code;

}
