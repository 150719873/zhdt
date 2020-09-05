package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryChildVo;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryVo;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryChildVo;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019年3月4日
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkCenterProcessDbFieldDto extends BaseDto {

	private String id;

	private String processId;

	private String processDbId;

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

	private String dbFieldId;

}
