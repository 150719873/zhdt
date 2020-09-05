package com.dotop.smartwater.project.module.core.water.bo;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同owner_bill
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerBillBo extends BaseBo {
	private String id;

	private String ownerid;

	private Double realwater;

	private Double realpay;

	private String createuser;

	private Date createtime;

}