package com.dotop.smartwater.project.module.core.water.form;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019年3月4日
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterDbForm extends BaseForm {

	private String id;

	private String formId;

	private String name;

	private String code;

	// AUTO EXTERNAL
	private String loadType;

	// LOAD NOT_LOAD
	private String loadStatus;

	private String sqlStr;

	private List<WorkCenterDbFieldForm> dbFields;
}
