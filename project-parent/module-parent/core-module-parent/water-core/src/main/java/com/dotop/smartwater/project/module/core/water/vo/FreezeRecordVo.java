package com.dotop.smartwater.project.module.core.water.vo;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同freeze_record
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class FreezeRecordVo extends BaseVo {

	private String id;

	private String userno;

	private String username;

	private String userphone;

	private String devno;

	private Double water;

	private Integer tapstatus;

	private String rssi;

	private Double lsnr;

	private Date readtime;

	private Integer year;

	private Integer month;

	private Integer type;

	private Date freezetime;

	private String ownerid;

}