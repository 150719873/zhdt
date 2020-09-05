package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @date 2019年2月21日
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceParametersDto extends BaseDto {
	/**
	 * 设备参数配置id
	 */

	private String deviceParId;

	/**
	 * 批次号
	 */
	private String serialNumber;

	/**
	 * 设备类型名称
	 */
	private String deviceName;

	/**
	 * 设备通讯方式 1LORA 2移动NB 3电信NB 4-全网通
	 */
	private String mode;

	/**
	 * 阀门类型 阀门类型（0 不带 1 带阀）
	 */
	private String valveType;

	/**
	 * 阀门状态(0 关阀 1 开阀)
	 */
	private String valveStatus;

	/**
	 * 计量单位(10L 100L .. )
	 */
	private String unit;
	/**
	 * 设备参数配置id
	 */
	private String sensorType;
	/**
	 * 设备参数类型 状态(0 停用 1 启用)
	 */
	private String status;
	
	/**产品ID*/
	private String productId;
	/**产品名称*/
	private String productName;
	/**口径*/
	private String caliber;
	/**nfc初始密码*/
	private String nfcInitPwd;
	/**通讯密码*/
	private String nfcComPwd;
}
