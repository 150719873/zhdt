package com.dotop.pipe.core.dto.product;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;

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
public class EnterpriseProductMapDto extends BasePipeDto {
	/**
	 * 关联主键
	 */
	private String mapId;

	// /**
	// * 企业ID
	// */
	// private String enterpriseId;

	/**
	 * 产品ID
	 */
	private String productId;
}
