package com.dotop.smartwater.project.module.core.water.bo.customize;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerOperateBo extends BaseBo {

	private String ownerid;
	private String newdevno;
	private String newuserno;
	private String installmonth;
	/** 水表用途 */
	private String purposeid;
	/** 收费种类ID */
	private String typeid;
	/** 用水减免 */
	private String reduceid;
	private String reason;
	private String descr;

}
