package com.dotop.smartwater.project.module.core.water.form;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * /先插入今天的用水总量,再计算更新今天的用水量 EveryDayWaterSchedule
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class EverydayWaterRecordForm extends BaseForm {

	private Double water;

	private String communityid;

	private Date ctime;

	private Double addWater;

}