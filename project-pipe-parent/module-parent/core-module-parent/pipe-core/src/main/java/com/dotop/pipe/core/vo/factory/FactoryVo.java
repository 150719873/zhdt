package com.dotop.pipe.core.vo.factory;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @date 2018/10/25.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FactoryVo extends BasePipeVo {
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
