package com.dotop.smartwater.project.module.core.water.form.customize;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 导入数据 AjaxUploadController
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class ImportFileForm extends BaseForm {

	private String filename;

	private String cid;

}
