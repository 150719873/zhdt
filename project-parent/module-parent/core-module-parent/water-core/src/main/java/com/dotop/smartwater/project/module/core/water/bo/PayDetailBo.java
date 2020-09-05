package com.dotop.smartwater.project.module.core.water.bo;

import java.math.BigDecimal;
import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同pay_detail
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class PayDetailBo extends BaseBo {

	private String id;

	private String ownerid;

	private String ownerno;

	private String ownername;

	private BigDecimal paymoney;

	private Date createtime;

	private String createuser;

	private String username;

	private Integer type;

	private BigDecimal beforemoney;

	private BigDecimal aftermoney;

	private String payno;

	private String tradeno;

	private String remark;

	private String ctime;

}