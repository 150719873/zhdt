package com.dotop.smartwater.project.module.core.water.bo;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DictionaryBo extends BaseBo {
	private String dictionaryId;

	private String dictionaryCode;

	private String dictionaryType;

	private String dictionaryName;

	private String dictionaryValue;

	private String remark;

	private String createBy;

	private Date createDate;

	private String lastBy;

	private Date lastDate;

	private Integer isDel;

	private List<DictionaryChildBo> children;

}