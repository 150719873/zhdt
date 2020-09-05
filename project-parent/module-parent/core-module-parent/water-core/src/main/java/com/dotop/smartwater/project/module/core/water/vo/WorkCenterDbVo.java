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
public class WorkCenterDbVo extends BaseVo {

	private String id;

	private String formId;

	private String name;

	private String code;

	private String loadType;

	private String loadStatus;

	private String sqlStr;

	private List<WorkCenterDbFieldVo> dbFields;

	// 是否有效
	private String ifEffect;
}
