package com.dotop.smartwater.project.module.core.water.dto;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同owner_cancel_record
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerCancelRecordDto extends BaseDto {

	private String id;
	private String communityid;
	private String ownerid;
	private String operateuser;
	private Date operatetime;

}
