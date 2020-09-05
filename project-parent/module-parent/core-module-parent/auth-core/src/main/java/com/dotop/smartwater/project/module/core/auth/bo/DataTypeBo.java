package com.dotop.smartwater.project.module.core.auth.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019/8/7.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DataTypeBo extends BaseBo{

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 数据类型名称
	 */
	private String name;
}
