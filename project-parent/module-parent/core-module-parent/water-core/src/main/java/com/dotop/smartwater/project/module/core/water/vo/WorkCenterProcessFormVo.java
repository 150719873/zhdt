package com.dotop.smartwater.project.module.core.water.vo;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.water.model.BodyMap;
import com.dotop.smartwater.project.module.core.water.model.BodyMap;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterProcessFormVo extends BaseVo {

	private String id;
	private String processId;

	private String code;
	private String name;
	private String body;
	private String appBody;
	private List<BodyMap> bodyMap;

	private String formId;

	private List<WorkCenterProcessDbVo> processDbs;
}
