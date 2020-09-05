package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 账单
 * 

 *
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderBo extends BaseBo {

	/* 账单ID */
	private String id;

	/* 业主ID */
	private String ownerid;

	/* 账单流水号 */
	private String tradeno;
	/* 账单年份 */
	private String year;
	/* 账单月份 */
	private String month;

	/* 区域ID */
	private String communityid;

	/* 区域编号 */
	private String communityno;
	/* 区域名称 */
	private String communityname;

	/* 用户编号 */
	private String userno;
	/* 用户名称 */
	private String username;
	/* 手机号 */
	private String phone;
	/* 身份证号码 */
	private String cardid;
	/* 地址 */
	private String addr;

	/* 水表EUI */
	private String deveui;
	/* 水表号 */
	private String devno;
	/* 设备状态 1-在线 2-离线 */
	private Integer devstatus;
	/* 状态补充说明 */
	private String explain;
	/* 阀门状态 1-开阀 2-关阀 */
	private Integer tapstatus;
	/* 阀门类型 1-带阀 0-不带阀 */
	private Integer taptype;

	/* 上期抄表日期 */
	private String upreadtime;
	/* 上期抄表读数 */
	private Double upreadwater;

	/* 本期抄表时间 */
	private String readtime;
	/* 本期抄表读数 */
	private Double readwater;
	/* 距离上期抄表天数 */
	private Integer day;
	/* 用水量 */
	private Double water;

	/* 交易流水号 */
	private String payno;
	/* 收费类型ID */
	private String pricetypeid;
	/* 收费类型名称 */
	private String pricetypename;

	/* 原始水费金额 */
	private BigDecimal original;

	/* 应缴金额 */
	private Double amount;
	/* 余额抵扣 */
	private Double balance;
	/* 实缴金额 */
	private Double realamount;
	/* 支付方式 1-现金 2-微信 3-支付宝 */
	private Integer paytype;
	/* 支付状态 1-已缴 0-未缴 */
	private Integer paystatus;
	/* 支付时间 */
	private String paytime;

	/* 滞纳金 */
	private Double penalty;

	/* 账单处理人ID */
	private String operatorid;
	/* 账单处理人姓名 */
	private String operatorname;
	/* 账单处理时间 */
	private String operatortime;

	/* 账单状态 */
	private Integer tradestatus;
	/* 状态描述 */
	private String describe;

	private Integer errortype;

	/* 账单生成人ID */
	private String generateuserid;
	/* 账单生成人姓名 */
	private String generateusername;
	/* 账单生成日期 */
	private String generatetime;

	/* 减免id */
	private String reduceid;

	/* 水费用途id */
	private String purposeid;

	// 优惠券金额
	private Double couponmoney;

	// 是否已经打印
	private Integer isprint;

	// 打印次数
	private Integer frequency;

	// 实收金额
	private Double ownerpay;

	// 找零
	private Double givechange;

	/** 标记异常 */
	private String remark;
	/** 标记异常状态 1-异常 2-撤销 */
	private Integer markstatus;

}
