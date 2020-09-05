package com.dotop.smartwater.project.module.core.auth.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模板数据
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TemplateDataVo extends BaseVo {
	
	/**文字信息*/
	private String value;
	/**自提颜色*/
	private String color;
	
	public TemplateDataVo(String value, String color) {
		super();
		this.value = value;
		this.color = color;
	}
}
