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
public class WorkCenterProcessHandleVo extends BaseVo {

	private String processId;

	private String body;

	private String appBody;

	private List<BodyMap> bodyMap;

	private List<WorkCenterDbVo> dbAutos;

	private List<WorkCenterProcessDbVo> processDbAutos;

	private WorkCenterProcessNodeVo currProcessNode;

	private List<List<String>> dbAutoColumns;

	private List<List<String>> dbAutoReturns;

	private Long dbAutoCount;
}
