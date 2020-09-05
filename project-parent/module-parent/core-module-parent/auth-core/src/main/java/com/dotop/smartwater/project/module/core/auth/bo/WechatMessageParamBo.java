package com.dotop.smartwater.project.module.core.auth.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @date 2018年7月12日 下午4:52:17
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatMessageParamBo extends BaseBo {

	private String bussinessname;

	/*
	 * 参照smsEnum
	 */
	private Integer messageState;

	/*
	 * 业主id
	 */
	private String ownerid;

	/*
	 * 企业编号
	 */
//	private String enterpriseid;

	/*
	 * 业主名称
	 */
	private String userName;

	/*
	 * 水表编号
	 */
	private String devno;

	/*
	 * 创建时间
	 */
	private String createtime;
	/*
	 * 用水量
	 */
	private Double water;

	/*
	 * 账单开始时间
	 */
	private String billBeginTime;

	/*
	 * 账单结束时间
	 */
	private String billEndTime;

	/*
	 * 水费
	 */
	private Double money;

	/*
	 * 账号余额
	 */
	private Double alreadypay;

	/*
	 * 备注
	 */
	private String remark;

	/*
	 * 企业名称
	 */
	private String enterpriseName;

	/*
	 * 账单流水号
	 */
	private String tradeno;

	/*
	 * 缴费时间
	 */
	private String tradetime;

	/*
	 * 缴费金额
	 */
	private Double trademoney;

	/*
	 * 账单月份
	 */
	private String billmonth;

	private String month;

	/*
	 * 充值时间
	 */
	private String rechargetime;

	/*
	 * 充值金额
	 */
	private Double rechargemoney;

	/*
	 * 发送类型，0或者null平台，1是微信
	 */
	private Integer sendType;

}
