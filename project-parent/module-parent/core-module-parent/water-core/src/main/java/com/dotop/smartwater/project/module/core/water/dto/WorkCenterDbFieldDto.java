package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019年3月4日
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterDbFieldDto extends BaseDto {

	private String id;

	private String dbId;

	private String attribute;

	private String name;

	// 字段类型
	private String fieldType;

	// 类型-类型id
	// private String typeDictChildId;
	private DictionaryChildDto typeDictChild;

	// 内容-类别id
	// private String contentDictId;
	private DictionaryDto contentDict;

	// 关联-类型id
	// private String relationDictChildId;
	private DictionaryChildDto relationDictChild;

}
