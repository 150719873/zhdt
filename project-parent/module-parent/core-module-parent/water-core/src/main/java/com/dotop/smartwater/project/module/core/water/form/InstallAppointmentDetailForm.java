package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报装-预约次数
 * 

 * @date 2019年3月8日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InstallAppointmentDetailForm extends BaseForm {
	/* 日期 */
	private String date;
	/* 已预约次数 */
	private String number;
	/* 开始时间 */
	private String startDate;
	/* 结束时间 */
	private String endDate;
	/* 最多预约天数 */
	private Integer appointmentDay;
	/* 每天最多预约个数 */
	private Integer appointmentNumber;

}
