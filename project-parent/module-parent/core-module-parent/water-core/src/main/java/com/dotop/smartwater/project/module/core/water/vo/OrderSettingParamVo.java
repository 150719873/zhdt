package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2018/11/23.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderSettingParamVo extends BaseVo {
	/**
	 * 系统ID：1营收系统，2管漏系统
	 */
	private String systemId;

	/**
	 * 类型：1报装，2报修，3巡检
	 */
	private String type;
}
