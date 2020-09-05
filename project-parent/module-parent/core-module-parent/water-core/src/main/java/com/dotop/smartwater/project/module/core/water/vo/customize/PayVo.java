package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

//表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class PayVo extends BaseVo {

	private String devno;

	private String ownerid;

	private String userno;

	private String username;

	private String useraddr;

	private String userphone;

	private String typename;

	private String communityname;

	// 应缴费用
	private Double mustpay;

	// 所欠费用
	private Double owepay;

	// 已交费用
	private Double alreadypay;

	// 剩余金额
	private Double surplus;

}