package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 抄表任务参数

 * @date 2019/2/22.
 * 抄表任务 meter_reading_task
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MeterReadingTaskForm extends BaseForm {
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

	private String createBy;

	private Date createDate;

	private String lastBy;

	private Date lastDate;

	private int isDel;
}
