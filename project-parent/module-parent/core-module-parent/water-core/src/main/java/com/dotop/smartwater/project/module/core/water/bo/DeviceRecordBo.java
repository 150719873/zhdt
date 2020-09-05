package com.dotop.smartwater.project.module.core.water.bo;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

//表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceRecordBo extends BaseBo {

	private String id;

	private String devid;

	private String olddevid;

	private String name;

	private String deveui;

	private String devaddr;

	private String devno;

	private String operateuser;

	private Date operatetime;

	private Integer flag;

	private Double olddata;

	private Double olddatabegin;

	private String reason;

	private String communityid;

	private String ownerid;

	private Double owewater;

	private Integer owestatus;

}