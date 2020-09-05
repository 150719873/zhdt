package com.dotop.pipe.core.form;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @date 2018/10/30.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EnterpriseProductMapForm extends BasePipeForm {
	/**
	 * 关联主键
	 */
	private String mapId;

	/**
	 * 企业ID
	 */
	// private String enterpriseId;

	/**
	 * 产品ID
	 */
	private String productId;

	/**
	 * 产品IDs
	 */
	private List<String> productIds;
}
