package com.dotop.smartwater.project.module.core.water.bo.customize;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同pricetype_set
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class PricetypeSetBo extends BaseBo {
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