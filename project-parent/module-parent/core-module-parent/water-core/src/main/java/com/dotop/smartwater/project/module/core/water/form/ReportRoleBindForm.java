package com.dotop.smartwater.project.module.core.water.form;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReportRoleBindForm extends BaseForm {

	private String id;

	private String roleid;

	private String bindid;

	private List<String> bindids;
}
