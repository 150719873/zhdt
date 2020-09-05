package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerOperateVo extends BaseVo {

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
