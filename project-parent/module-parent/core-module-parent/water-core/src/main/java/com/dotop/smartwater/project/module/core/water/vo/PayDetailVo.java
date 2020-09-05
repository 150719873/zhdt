package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同pay_detail
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class PayDetailVo extends BaseVo {

	private String devno;

	private String ownerid;

	private String userno;

	private String username;

	private String useraddr;

	private String userphone;

	private String typename;

	private String communityname;

	private String createuser;

	private String paytime;

	// 明细金额
	private Double paymoney;

}