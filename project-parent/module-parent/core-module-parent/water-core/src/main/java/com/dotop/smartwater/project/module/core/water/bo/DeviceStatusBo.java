package com.dotop.smartwater.project.module.core.water.bo;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

//表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceStatusBo extends BaseBo {
	private String id;

	private String deveui;

	private String devid;

	private Integer tapstatus;

	private String water;

	private Date updatetime;

}