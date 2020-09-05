package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 费用组成
 * 

 *
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class CompriseDto extends BaseDto {

	private String id;

	private String typeid;

	private String name;

	private String ratio;

	private double price;

	private double unitprice;

	private String starttime;

	private Integer enable;

	private Integer print;

	private String remark;

	private String createtime;

}
