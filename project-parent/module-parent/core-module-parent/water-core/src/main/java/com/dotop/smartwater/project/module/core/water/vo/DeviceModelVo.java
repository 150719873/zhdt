package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019/2/26.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceModelVo extends BaseVo {
	/**
	 * ID
	 */
	private String id;
	/**
	 * 型号
	 */
	private String type;
	/**
	 * 口径
	 */
	private String caliber;
	/**
	 * 最大读数
	 */
	private Double maxwater;
	/**
	 * 周期
	 */
	private Integer cycle;
	/**
	 * 创建人
	 */
	private String createuser;
	/**
	 * 创建人名称
	 */
	private String account;
	/**
	 * 创建人
	 */
	private String username;
	/**
	 * 创建时间
	 */
	private String createtime;
}
