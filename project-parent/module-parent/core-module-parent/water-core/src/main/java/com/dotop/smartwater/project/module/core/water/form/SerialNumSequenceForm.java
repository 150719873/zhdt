package com.dotop.smartwater.project.module.core.water.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流水号 同serial_num_sequence
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class SerialNumSequenceForm extends BaseForm {

	private String id;

	private String frist;

	private String middle;

	private String ending;

	private Long enable;

}