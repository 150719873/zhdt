package com.dotop.smartwater.project.module.core.water.form.customize;

import javax.servlet.http.HttpServletRequest;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信请求参数实体类 ---- ------ 发现之前的实体类 中有很少的字段 现根据需求 整合到同一个实体类中 ------
 * 

 * @date 2019年3月22日
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatParamForm extends BaseForm {

	private String ownerid;
	private String status;

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

	HttpServletRequest request;
}
