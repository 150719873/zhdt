package com.dotop.pipe.core.vo.product;

import java.util.Date;

import com.dotop.pipe.auth.core.vo.enterprise.EnterpriseVo;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @date 2018/10/30.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EnterpriseProductMapVo extends BasePipeVo {
	/**
	 * 关联主键
	 */
	private String mapId;

	/**
	 * 企业ID
	 */
	private EnterpriseVo enterprise;

	/**
	 * 产品
	 */
	private ProductVo product;

	/**
	 * 最后修改人
	 */
	private String lastBy;

	/**
	 * 最后修改时间
	 */
	private Date lastDate;

}
