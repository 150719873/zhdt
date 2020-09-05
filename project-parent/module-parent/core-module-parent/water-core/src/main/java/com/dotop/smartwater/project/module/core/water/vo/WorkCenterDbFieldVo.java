package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019年3月4日
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterDbFieldVo extends BaseVo {

	private String id;

	private String dbId;

	private String attribute;

	private String name;

	// 字段类型
	private String fieldType;

	// 类型-类型id
	// private String typeDictChildId;
	private DictionaryChildVo typeDictChild;

	// 内容-类别id
	// private String contentDictId;
	private DictionaryVo contentDict;

	// 关联-类型id
	// private String relationDictChildId;
	private DictionaryChildVo relationDictChild;

}
