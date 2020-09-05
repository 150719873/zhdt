package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取文件流Form

 * @date 2019-08-21 15:30
 *
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class FilePathForm extends BaseForm {
	private String path;
}
