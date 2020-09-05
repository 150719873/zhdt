package com.dotop.pipe.core.model;

import com.dotop.smartwater.dependence.core.common.RootModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @date 2018/10/25.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FactoryModel extends RootModel {

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
}
