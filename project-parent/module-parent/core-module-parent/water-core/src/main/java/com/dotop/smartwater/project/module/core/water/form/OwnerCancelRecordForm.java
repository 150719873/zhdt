package com.dotop.smartwater.project.module.core.water.form;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同owner_cancel_record
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerCancelRecordForm extends BaseForm {

	private String id;
	private String communityid;
	private String ownerid;
	private String operateuser;
	private Date operatetime;

}
