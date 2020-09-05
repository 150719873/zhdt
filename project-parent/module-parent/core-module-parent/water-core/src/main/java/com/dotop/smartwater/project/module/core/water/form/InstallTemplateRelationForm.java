package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报装-模板功能关系表
 * 

 * @date 2019年3月8日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InstallTemplateRelationForm extends BaseForm {
	/** 主键 */
	private String id;
	/** 模板ID */
	private String templateId;
	/** 功能ID */
	private String functionId;
	/** 序号 */
	private String no;
	/** 说明 */
	private String explain;
	/** 上传模板 */
	private String uploadFile;
}
