package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同freeze_record
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class FreezeForm extends BaseForm {

	private String id;
	private String userno;
	private String username;
	private String devno;
	private String water;
	private String phone;
	private String status;
	private String rxtime;
	private Long rssi;
	private Double lsnr;
	private Integer year;
	private Integer month;
	private String type;

	private String communityid;
	/** 年份、月份参数接收 */
	private String yr;
	private String mt;

	private String freezetime;
	private String readtime;
	private String tapstatus;

	public String getYr() {
		return (yr != null && !yr.equals("")) ? yr.substring(0, 4) : "";
	}

}
