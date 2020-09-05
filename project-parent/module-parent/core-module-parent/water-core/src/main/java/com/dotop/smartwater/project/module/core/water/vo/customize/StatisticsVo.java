package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 统计报表对象

 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StatisticsVo extends OrderVo {
	
	private String name;
	
	private Integer value;
	
}
