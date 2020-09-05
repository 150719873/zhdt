package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019/4/1.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceWarningSettingVo extends BaseVo{

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
	 * 通知人
	 */
	private String notifyUser;

	/**
	 * 流程模板id
	 */
	private String tmplId;

}
