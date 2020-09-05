package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**

 * @date 2019/4/1.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceWarningSettingForm extends BaseForm{
	/**
	 * 主键
	 */
	private String id;

	/**
	 * 告警类型
	 */
	private int warningType;

	/**
	 * 告警次数
	 */
	private int warningNum;

	/**
	 * 通知类型
	 */
	private int notifyType;

	/**
	 * 模板类型
	 */
	private int modelType;

	/**
	 * 通知人类型
	 */
	private String notifyUserType;

	/**
	 * 流程模板id
	 */
	private String tmplId;

	/**
	 * 通知人
	 */
	private String notifyUser;

	private String createBy;

	private Date createDate;

	private String lastBy;

	private Date lastDate;

	private int isDel;

}
