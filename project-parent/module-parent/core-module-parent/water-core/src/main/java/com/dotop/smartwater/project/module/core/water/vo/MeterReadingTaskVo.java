package com.dotop.smartwater.project.module.core.water.vo;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019/2/22. 抄表任务 meter_reading_task
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MeterReadingTaskVo extends BaseVo {
	/**
	 * 主键
	 */
	private String id;

	/**
	 * 批次号
	 */
	private String batchId;

	/**
	 * 任务名称
	 */
	private String taskName;

	/**
	 * 区域
	 */
	private String area;

	/** 区域ID */
	private String areaIds;

	/**
	 * 任务开始时间
	 */
	private Date taskStartTime;

	/**
	 * 任务截止时间
	 */
	private Date taskEndTime;

	/**
	 * 抄表人员
	 */
	private int readerNum;

	/**
	 * 完成情况
	 */
	private Integer status;

	/**
	 * 完成数
	 */
	private int finishedCount;

	/**
	 * 设备数
	 */
	private int deviceCount;

	/**
	 * 抄表人员名称
	 */
	private String meterReader;
}
