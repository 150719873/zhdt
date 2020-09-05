package com.dotop.smartwater.project.module.core.water.bo;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * /先插入今天的用水总量,再计算更新今天的用水量 EveryDayWaterSchedule
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class EverydayWaterRecordBo extends BaseBo {

	private Double water;

	private String communityid;

	private Date ctime;

	private Double addWater;

}