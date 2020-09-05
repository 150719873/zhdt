package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2018/11/23.
 * 工单配置结果
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderSettingValBo extends BaseBo {
	/**
	 * 拍照最少张数
	 */
	private Integer minimumPicture;
	/**
	 * 说明最少字数
	 */
	private Integer minimumWords;
	/**
	 * 状态
	 */
	private String[] status;
}
