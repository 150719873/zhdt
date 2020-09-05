package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 

 *
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class PayDetailRecord extends BaseVo {

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
