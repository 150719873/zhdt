package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @Title: CommunityWater.java
 * @Package com.dotop.water.dc.mapper.device
 * @Description: 首页：统计当前水司下每个月小区用水量Vo

 * @date 2018年5月23日 上午10:50:17
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class CommunityWaterVo extends BaseVo {

	private Double water;// 小区总用水量

	private String waterMonth;// 用水月份

}
