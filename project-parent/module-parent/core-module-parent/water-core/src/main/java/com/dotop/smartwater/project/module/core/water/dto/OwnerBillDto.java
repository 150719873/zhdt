package com.dotop.smartwater.project.module.core.water.dto;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同owner_bill
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerBillDto extends BaseDto {
	private String id;

	private String ownerid;

	private Double realwater;

	private Double realpay;

	private String createuser;

	private Date createtime;

}