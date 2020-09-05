package com.dotop.smartwater.project.module.core.water.form.customize;

import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterBuildForm extends BaseForm {

	/**
	 * 业务id
	 */
	private String businessId;
	/**
	 * 业务类型-字典id
	 */
	private String businessType;
	/**
	 * 模板id
	 */
	private String tmplId;
	/**
	 * sql参数(key-val)
	 */
	private Map<String, String> sqlParams;
	/**
	 * 自动显示参数(key-val)
	 */
	private Map<String, String> showParams;
	/**
	 * 运载参数(key-val)
	 */
	private Map<String, String> carryParams;

}
