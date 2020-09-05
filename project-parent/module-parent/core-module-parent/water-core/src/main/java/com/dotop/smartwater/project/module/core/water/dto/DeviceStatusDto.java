package com.dotop.smartwater.project.module.core.water.dto;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

//表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceStatusDto extends BaseDto {
	private String id;

	private String deveui;

	private String devid;

	private Integer tapstatus;

	private String water;

	private Date updatetime;

}