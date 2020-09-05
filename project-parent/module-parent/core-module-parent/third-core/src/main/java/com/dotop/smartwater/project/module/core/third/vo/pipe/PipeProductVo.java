package com.dotop.smartwater.project.module.core.third.vo.pipe;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PipeProductVo {

	/**
	 * 产品ID
	 */
	private String productId;

	/**
	 * 厂商
	 */
	private PipeFactoryVo factory;

	/**
	 * 产品类别
	 */
	private PipeDictionaryVo category;

	/**
	 * 产品类型
	 */
	private PipeDictionaryVo type;

	/**
	 * 产品编码
	 */
	private String code;

	/**
	 * 产品名称
	 */
	private String name;

	/**
	 * 产品描述
	 */
	private String des;

	/**
	 * 口径
	 */
	private PipeDictionaryVo caliber;

	/**
	 * 材质
	 */
	private PipeDictionaryVo material;

	/**
	 * 版本
	 */
	private String version;

	/**
	 * 最后修改人
	 */
	private String lastBy;

	/**
	 * 最后修改时间
	 */
	private Date lastDate;

}
