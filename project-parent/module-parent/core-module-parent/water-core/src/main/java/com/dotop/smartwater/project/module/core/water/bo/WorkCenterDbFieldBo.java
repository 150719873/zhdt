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
public class WorkCenterDbFieldBo extends BaseBo {

	private String id;

	private String dbId;

	private String attribute;

	private String name;

	// 字段类型
	private String fieldType;

	// 类型-类型id
	// private String typeDictChildId;
	private DictionaryChildBo typeDictChild;

	// 内容-类别id
	// private String contentDictId;
	private DictionaryBo contentDict;

	// 关联-类型id
	// private String relationDictChildId;
	private DictionaryChildBo relationDictChild;

}
