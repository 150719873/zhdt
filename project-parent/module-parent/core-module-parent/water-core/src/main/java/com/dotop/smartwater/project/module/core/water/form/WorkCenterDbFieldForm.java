package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019年3月4日
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterDbFieldForm extends BaseForm {

	private String id;

	private String dbId;

	private String attribute;

	private String name;

	// 字段类型 COMMON RELATION
	private String fieldType;

	// 类型-类型id
	// private String typeDictChildId;
	private DictionaryChildForm typeDictChild;

	// 内容-类别id
	// private String contentDictId;
	private DictionaryForm contentDict;

	// 关联-类型id
	// private String relationDictChildId;
	private DictionaryChildForm relationDictChild;

}
