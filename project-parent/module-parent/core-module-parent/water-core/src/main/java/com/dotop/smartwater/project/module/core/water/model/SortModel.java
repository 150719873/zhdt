package com.dotop.smartwater.project.module.core.water.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 排序公共实体类
 * @create: 2020-05-18 14:49
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class SortModel {
	//排序字段名称
	private String sortName;
	//排序 desc或者asc
	private String sortValue;
}
