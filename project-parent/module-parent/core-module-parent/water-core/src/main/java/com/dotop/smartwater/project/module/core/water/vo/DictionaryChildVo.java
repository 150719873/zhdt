package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DictionaryChildVo extends BaseVo {
	private String childId;

	private String dictionaryId;

	private String childName;

	private String childValue;

	private String remark;

}