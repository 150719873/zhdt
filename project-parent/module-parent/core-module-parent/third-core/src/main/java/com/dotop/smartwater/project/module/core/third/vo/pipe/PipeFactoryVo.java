package com.dotop.smartwater.project.module.core.third.vo.pipe;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PipeFactoryVo {
	/**
	 * 厂商ID
	 */
	private String factoryId;

	/**
	 * 厂商编码
	 */
	private String code;

	/**
	 * 厂商名称
	 */
	private String name;

	/**
	 * 厂商描述
	 */
	private String des;

	/**
	 * 最后修改人
	 */
	private String lastBy;

	/**
	 * 最后修改时间
	 */
	private Date lastDate;
}
