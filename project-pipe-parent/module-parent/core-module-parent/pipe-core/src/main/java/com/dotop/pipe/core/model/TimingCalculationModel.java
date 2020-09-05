package com.dotop.pipe.core.model;

import com.dotop.pipe.auth.core.model.BaseEnterpriseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 定时计算公式
 * 
 *
 * @date 2018年12月12日
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TimingCalculationModel extends BaseEnterpriseModel {

	/**
	 * 主键
	 */
	private String caId;

	/**
	 * 编号
	 */
	private String code;

	/**
	 * 名字
	 */
	private String name;

	/**
	 * 描述
	 */
	private String des;

	/**
	 * 是否使用
	 */
	private String status;

	/**
	 * 上限
	 */
	private String upperLimit;

	/**
	 * 下限
	 */
	private String lowerLimit;

}
