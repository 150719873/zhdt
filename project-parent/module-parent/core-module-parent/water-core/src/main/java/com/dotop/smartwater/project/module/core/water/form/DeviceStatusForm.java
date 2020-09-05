package com.dotop.smartwater.project.module.core.water.form;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

//表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceStatusForm extends BaseForm {
	private String id;

	private String deveui;

	private String devid;

	private Integer tapstatus;

	private String water;

	private Date updatetime;

}