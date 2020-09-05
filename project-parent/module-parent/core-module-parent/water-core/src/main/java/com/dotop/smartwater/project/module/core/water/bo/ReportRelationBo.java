package com.dotop.smartwater.project.module.core.water.bo;

import java.math.BigDecimal;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * 报表展示设计报表序列Bo

 * @date 2019-07-22
 *
 */

//表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class ReportRelationBo extends BaseBo {
	
	//主键
	private String reportRelationId;
	//报表设计ID
	private String reportDesignId;
	//报表ID
	private String reportid;
	//序号（报表排列的顺序）
	private BigDecimal orderCode;
	//每个报表的size
	private Integer size;
	//行高
	private Integer height;
}
