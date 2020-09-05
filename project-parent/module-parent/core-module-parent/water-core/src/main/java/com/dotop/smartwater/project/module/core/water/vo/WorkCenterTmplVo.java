package com.dotop.smartwater.project.module.core.water.vo;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019年3月4日
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterTmplVo extends BaseVo {

	private String id;
	private String code;
	private String name;
	private String desc;

	private WorkCenterFormVo form;

	private List<WorkCenterTmplNodeVo> tmplNodes;

	private List<WorkCenterTmplNodePointVo> nodes;

	private List<WorkCenterTmplNodeEdgeVo> edges;

	// 是否有效
	private String ifEffect;

}
