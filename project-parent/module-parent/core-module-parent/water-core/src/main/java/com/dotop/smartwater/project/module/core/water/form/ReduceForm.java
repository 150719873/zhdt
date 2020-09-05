package com.dotop.smartwater.project.module.core.water.form;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同reduce
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class ReduceForm extends BaseForm {
	private String reduceid;
	private String name;
	private Integer unit;
	private Double rvalue;
	private Date ctime;
	private String cuser;

}