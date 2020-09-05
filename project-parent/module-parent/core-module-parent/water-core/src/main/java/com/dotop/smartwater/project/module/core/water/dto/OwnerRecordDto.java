package com.dotop.smartwater.project.module.core.water.dto;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同owner_record
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerRecordDto extends BaseDto {

	private String id;
	private String communityid;// 区域ID
	private Integer type;// 类型 1-销户 2-过户 3-换表
	private String ownerid;// 报装ID
	private String username;// 业主姓名
	private String oldownerid;// 原业主ID
	private String oldusername;// 原业主姓名
	private String olduserphone;// 原业主电话
	private String devid;// 水表ID
	private String devno;// 水表编号
	private Double devnum;// 水表读数
	private String olddevid;// 原水表ID
	private String olddevno;// 原水表编号
	private Double olddevnum;// 原水表读数
	private Double oldalreadypay;// 原余额
	private String operateuser;// 操作用户
	private String operatename;
	private Date operatetime;// 操作时间
	private String reason;// 原因
	private String descr;// 说明

}