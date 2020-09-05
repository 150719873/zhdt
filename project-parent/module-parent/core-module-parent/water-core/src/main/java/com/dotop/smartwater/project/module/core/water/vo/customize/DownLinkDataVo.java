package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备校准下发
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class DownLinkDataVo extends BaseVo {

	private String measureMethod;
	private String measureValue;
	private String measureType;
	private String measureUnit;
	private String networkInterval;

}
