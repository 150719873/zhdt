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
public class WorkCenterProcessDbFieldForm extends BaseForm {

	private String id;

	private String processId;

	private String processDbId;

	private String attribute;

	private String name;

	// 字段类型
	private String fieldType;

	// 类型-类型id
	private String typeDictChildId;

	// 内容-类别id
	private String contentDictId;

	// 关联-类型id
	private String relationDictChildId;

	private String dbFieldId;

}
