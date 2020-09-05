package com.dotop.smartwater.project.module.core.water.bo;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019年3月4日
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterTmplBo extends BaseBo {

	private String id;
	private String code;
	private String name;
	private String desc;

	private WorkCenterFormBo form;

	private List<WorkCenterTmplNodeBo> tmplNodes;

	private List<WorkCenterTmplNodePointBo> nodes;

	private List<WorkCenterTmplNodeEdgeBo> edges;

	// 是否有效
	private String ifEffect;
}
