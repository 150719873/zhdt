package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 抄表任务
 * 

 * @date 2019年4月5日
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IssuedMeterVo extends BaseVo {
	/** 任务名称 */
	private String name;
	/** 区域ID多个 */
	private String communityids;
	/** 任务截止日期 */
	private String endTime;
}
