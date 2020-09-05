package com.dotop.smartwater.project.module.core.water.bo;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同owner_cancel_record
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerCancelRecordBo extends BaseBo {

	private String id;
	private String communityid;
	private String ownerid;
	private String operateuser;
	private Date operatetime;

}
