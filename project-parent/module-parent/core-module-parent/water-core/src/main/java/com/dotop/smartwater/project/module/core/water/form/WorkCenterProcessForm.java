package com.dotop.smartwater.project.module.core.water.form;

import java.util.Date;
import java.util.List;
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
public class WorkCenterProcessForm extends BaseForm {

	private List<String> statuss;

	private String id;

	private String code;

	private String title;

	private String businessId;

	private String businessType;

	private Map<String, String> sqlParams;

	private Map<String, String> showParams;

	private Map<String, String> fillParams;

	private Map<String, String> carryParams;

	private String status;

	private String applicant;

	private String applicantName;

	private Date applicationDate;

	private String processFormId;

	/**
	 * 原模板id
	 */
	private String tmplId;

	private Date startDate;

	private Date endDate;

}
