package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流水号 同serial_num_sequence
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class SerialNumSequenceBo extends BaseBo {

	private String id;

	private String frist;

	private String middle;

	private String ending;

	private Long enable;

}