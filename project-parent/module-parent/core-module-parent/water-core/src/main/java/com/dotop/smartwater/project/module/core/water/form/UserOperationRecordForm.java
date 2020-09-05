package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 同user_operation_record
 * 用户操作记录
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class UserOperationRecordForm extends BaseForm {

	/**
	 * id
	 */
	private String id;
	/**
	 * 操作人账号
	 */
	private String operateuser;
	/**
	 * 操作
	 */
	private String operate;
	/**
	 * 操作时间
	 */
	private String operatetime;
	/**
	 * 用户id
	 */
	private String userid;
	/**
	 * 操作人名称
	 */
	private String operateusername;

	/**
	 * 操作人ip
	 */
	private String ip;

	/**
	 * 操作类型
	 */
	private String operatetype;

	private Date startTime;

	private Date endTime;
}
