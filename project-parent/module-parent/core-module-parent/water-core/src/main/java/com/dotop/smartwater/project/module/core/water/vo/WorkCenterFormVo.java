package com.dotop.smartwater.project.module.core.water.vo;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.water.model.BodyMap;
import com.dotop.smartwater.project.module.core.water.model.BodyMap;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019年3月4日
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterFormVo extends BaseVo {

	private String id;
	private String code;
	private String name;
	private String desc;
	private String body;
	private String appBody;
	private List<BodyMap> bodyMap;

	private List<WorkCenterDbVo> dbs;

	// 是否有效
	private String ifEffect;

}
