package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @date 2019年2月26日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PerforWeightBo extends BaseBo {

	/** 主键 */
	private String id;
	/** 标题 */
	private String title;
	/** 描述 */
	private String describe;
	/** 分数 */
	private String score;

}
