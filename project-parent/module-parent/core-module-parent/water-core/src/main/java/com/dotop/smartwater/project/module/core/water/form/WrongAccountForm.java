package com.dotop.smartwater.project.module.core.water.form;

import java.math.BigDecimal;
import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class WrongAccountForm extends BaseForm {

	// 主键
	private String id;
	// 错账单号
	private String wrongno;

	// 企业id
	private String enterpriseid;

	// 错账处理状态
	private Integer status;
	// 错账申请方式
	private Integer type;
	// 错账申请时间
	private Date applytime;
	// 错账描述
	private String description;

	// 申请人id(使用账单用户信息)
	private String ownerid;

	// 区域Id(使用区域信息)
	private String communityid;

	// 账单id
	private String orderid;

	// 处理人
	private String handleid;
	// 处理人名
	private String handlename;
	// 处理时间
	private Date handletime;
	// 处理描述
	private String handedesc;

	// 代金抵扣券id
	private String couponid;

	// 创建人
	private String createuser;
	// 创建时间
	private Date createtime;

	// 冗余字段
	// 账单金额
	private BigDecimal original;
	// 区域编号
	private String communityno;
	// 区域名称
	private String communityname;
	// 账单流水号
	private String tradeno;
	// 账单年份
	private String year;
	// 账单月份
	private String month;
	// 业主编号
	private String userno;
	// 业主名称
	private String username;
	// 电话
	private String phone;
	// 地址
	private String addr;
	// 收费种类名称
	private String pricetypename;
	// 上次抄表日期
	private Date upreadtime;
	// 上次抄表读数
	private BigDecimal upreadwater;
	// 本期抄表时间
	private Date readtime;
	// 本期抄表读数
	private BigDecimal readwater;
	// 用水量（吨）
	private BigDecimal water;
	// 交易流水号
	private String payno;
	// 原始金额(纯梯度水费组成)
	private BigDecimal amount;
	// 余额抵扣
	private BigDecimal balance;
	// 实缴金额
	private BigDecimal realamount;
	// 支付方式（1-现金支付 2-微信支付 3-支付宝支付）
	private Integer paytype;
	// 缴费状态（0-未缴 1-已缴）
	private Integer paystatus;
	// 缴费时间
	private Date paytime;
	// 账单状态（1-正常 0-异常）
	private Integer tradestatus;
	// 打印状态(0没打印 1已打印)
	private Integer isprint;

	// 滞纳金
	private BigDecimal penalty;

	// 代金抵扣券编号
	private String couponno;
	// 代金抵扣券类型
	private Integer coupontype;
	// 代金抵扣券名称
	private String couponname;
	// 代金抵扣券开始时间(yyyy-MM-dd)
	private Date couponstarttime;
	// 代金抵扣券结束时间(yyyy-MM-dd)
	private Date couponendtime;
	// 代金抵扣券面值
	private BigDecimal facevalue;
	// 代金抵扣券单位
	private Integer couponunit;
	// 代金抵扣券状态，0:未使用(正常状态),1:不可用(未到开始时间),2:已失效,3:已使用，4：已过期
	private Integer couponstatus;

	// 减免名称
	private String reducename;
	// 减免单位类型 1为元 2为吨
	private Integer reduceunit;
	// 减免值
	private BigDecimal reducervalue;

	// 区域communityIds(使用区域信息)
	private String communityIds;

}
