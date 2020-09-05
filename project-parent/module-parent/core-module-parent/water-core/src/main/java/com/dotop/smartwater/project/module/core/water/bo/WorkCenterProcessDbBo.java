package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019年3月4日
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterProcessDbBo extends BaseBo {

	private String id;

	private String processId;

	private String processFormId;

	private String name;

	private String loadType;

	private String loadStatus;

	private String sqlStr;

	private String dbId;

}
