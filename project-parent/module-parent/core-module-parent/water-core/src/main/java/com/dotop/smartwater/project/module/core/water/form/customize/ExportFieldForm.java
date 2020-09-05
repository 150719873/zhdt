package com.dotop.smartwater.project.module.core.water.form.customize;


import com.dotop.smartwater.dependence.core.common.BaseForm;

/**
 * 导出字段接收
 */
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ExportFieldForm extends BaseForm {
	
	private String key;
	
	private String name;
	
}
