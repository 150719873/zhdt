package com.dotop.pipe.core.model;

import com.dotop.smartwater.dependence.core.common.RootModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @date 2018/10/30.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnterpriseProductMapModel extends RootModel {
	/**
	 * 关联主键
	 */
	private String mapId;

	/**
	 * 企业ID
	 */
	private String enterpriseId;

	/**
	 * 产品ID
	 */
	private String productId;
}
