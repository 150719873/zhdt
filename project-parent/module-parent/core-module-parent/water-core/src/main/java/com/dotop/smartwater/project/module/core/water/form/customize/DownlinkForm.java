package com.dotop.smartwater.project.module.core.water.form.customize;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class DownlinkForm extends BaseForm {
	private Integer command;
	private String deveui;
	private String devid;
	private String value;
	private String reason;
	private String name;
	private String devno;
	private String measurementMethods;
	private String measurementValue;
	private String measurementType;
	private String measurementUnit;
	private String networkInterval;
	private String life;
	private String period;
	private String expire;
}