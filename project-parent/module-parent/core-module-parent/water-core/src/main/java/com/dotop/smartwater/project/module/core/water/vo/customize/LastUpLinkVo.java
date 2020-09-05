package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class LastUpLinkVo extends BaseVo {

	private String devid;
	private String rxtime;
	private Double water;
	private Integer days;

}
