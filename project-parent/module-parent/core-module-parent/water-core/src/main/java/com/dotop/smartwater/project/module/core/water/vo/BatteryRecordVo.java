package com.dotop.smartwater.project.module.core.water.vo;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 低电量水表换电池
 * 
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class BatteryRecordVo extends BaseVo {

	private String id;
	private String deveui;
	private String devno;
	private String operateuser;
	private Date operatetime;

}
