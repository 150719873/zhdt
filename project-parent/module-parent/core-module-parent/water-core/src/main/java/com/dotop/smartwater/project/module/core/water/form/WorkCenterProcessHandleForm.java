package com.dotop.smartwater.project.module.core.water.form;

import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019年3月4日
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterProcessHandleForm extends BaseForm {

	private String processId;
	private String title;
	private String tmplId;
	private String businessId;
	private String businessType;
	private Map<String, String> sqlParams;
	private Map<String, String> showParams;
	private Map<String, String> fillParams;
	private Map<String, String> carryParams;

	private String dbId;
	private String processDbId;
}
