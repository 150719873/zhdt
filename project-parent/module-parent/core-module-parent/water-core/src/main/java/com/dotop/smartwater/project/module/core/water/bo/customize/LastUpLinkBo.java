package com.dotop.smartwater.project.module.core.water.bo.customize;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class LastUpLinkBo extends BaseBo {

	private String devid;
	private String rxtime;
	private Double water;
	private Integer days;

}
