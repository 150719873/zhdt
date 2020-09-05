package com.dotop.pipe.core.model;

import com.dotop.pipe.auth.core.model.BaseEnterpriseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 定时计算公式内容项
 * 
 *
 * @date 2018年12月12日
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TimingFormulaModel extends BaseEnterpriseModel {

	/**
	 * 主键
	 */
	private String tfId;

	/**
	 * 设备主键
	 */
	private String deviceId;

	/**
	 * 方向
	 */
	private String direction;

	/**
	 * 倍数
	 */
	private String multiple;

	/**
	 * 公式主键
	 */
	private String tcId;
}
