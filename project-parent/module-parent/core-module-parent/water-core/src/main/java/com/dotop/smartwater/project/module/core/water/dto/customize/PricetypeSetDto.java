package com.dotop.smartwater.project.module.core.water.dto.customize;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同pricetype_set
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class PricetypeSetDto extends BaseDto {
	private String pricetypeid;

	private String typename;

	private Double limitone;

	private Double priceone;

	private Double limittwo;

	private Double pricetwo;

	private Double limitthree;

	private Double pricethree;

	private Double limitfour;

	private Double pricefour;

	private Double limitfive;

	private Double pricefive;

	private Double limitsix;

	private Double pricesix;

	private String createuser;

	private Date createtime;

}