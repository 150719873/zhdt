package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2018/11/23.
 * 工单配置结果
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderSettingValVo extends BaseVo {
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
