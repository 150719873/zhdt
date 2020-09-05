package com.dotop.smartwater.project.module.core.water.bo.customize;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备校准下发
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class DownLinkDataBo extends BaseBo {

	private String measureMethod;
	private String measureValue;
	private String measureType;
	private String measureUnit;
	private String networkInterval;
	private String reason;
	// 新增的下发任务
	private String life;
	private String period;


}
