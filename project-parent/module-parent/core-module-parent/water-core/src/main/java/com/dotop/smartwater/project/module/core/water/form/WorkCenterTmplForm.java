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
public class WorkCenterTmplForm extends BaseForm {

	private String id;
	private String code;
	private String name;
	private String desc;

	private WorkCenterFormForm form;

	private List<WorkCenterTmplNodeForm> tmplNodes;

	private List<WorkCenterTmplNodePointForm> nodes;

	private List<WorkCenterTmplNodeEdgeForm> edges;
}
