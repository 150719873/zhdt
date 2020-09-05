package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class DictionaryVo extends BaseVo {
	private String dictionaryId;

	private String dictionaryCode;

	private String dictionaryType;

	private String dictionaryName;

	private String dictionaryValue;

	private String remark;

	List<DictionaryChildVo> children;

}